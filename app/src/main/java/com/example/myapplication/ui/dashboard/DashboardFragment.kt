package com.example.myapplication.ui.dashboard

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.adapter.ItemAdapter
import com.example.myapplication.databinding.FragmentDashboardBinding
import com.example.myapplication.model.Item
import okhttp3.*
import org.json.JSONObject

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    private lateinit var itemAdapter: ItemAdapter
    private lateinit var dashboardViewModel: DashboardViewModel

    private lateinit var webSocket: WebSocket

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dashboardViewModel =
            ViewModelProvider(this).get(DashboardViewModel::class.java)

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Set up RecyclerView
        setupRecyclerView()

        // Observe the LiveData from ViewModel and update the adapter's data
        dashboardViewModel.itemList.observe(viewLifecycleOwner) { items ->
            // Debug log to verify the items
            items.forEach { Log.d("DashboardFragment", "Item: ${it.title}, Temp: ${it.temperature}") }
            itemAdapter.submitList(items)
        }

        // Connect to ROS bridge and subscribe to motor topics
        connectToRosBridge()

        return root
    }

    private fun setupRecyclerView() {
        val recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Initialize the Adapter
        itemAdapter = ItemAdapter { item ->
            // Handle item click by navigating to ItemDetailFragment
            val action = DashboardFragmentDirections
                .actionNavigationDashboardToItemDetailFragment(
                    item.title,
                    item.id.toString(),
                    item.temperature.toString(),
                    item.position ?: "No Position",
                    item.velocity.toString()
                )
            findNavController().navigate(action)
        }
        recyclerView.adapter = itemAdapter
    }

    private fun connectToRosBridge() {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("ws://10.0.2.2:9090") // Emulator connects to host machine
            .build()
        webSocket = client.newWebSocket(request, object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                subscribeToMotorTopics()
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                handleIncomingMotorData(text)
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                activity?.runOnUiThread {
                    Toast.makeText(requireContext(), "Failed to connect to ROS bridge", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun subscribeToMotorTopics() {
        val topics = listOf(
            "motor1/temp", "motor1/pos", "motor1/vel",
            "motor2/temp", "motor2/pos", "motor2/vel",
            "motor3/temp", "motor3/pos", "motor3/vel",
            "motor4/temp", "motor4/pos", "motor4/vel"
        )
        for (topic in topics) {
            val message = JSONObject().apply {
                put("op", "subscribe")
                put("topic", topic)
                put("type", "std_msgs/msg/Float64") // Adjust based on actual message type
            }
            webSocket.send(message.toString())
        }
    }

    private fun handleIncomingMotorData(message: String) {
        val json = JSONObject(message)
        val topic = json.optString("topic")
        val value = json.getJSONObject("msg").optDouble("data", 0.0)

        // Map topic to motor and field
        dashboardViewModel.updateMotorData(topic, value)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        webSocket.close(1000, null)
    }
}
