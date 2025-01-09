package com.example.myapplication.ui.Joystick

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.databinding.FragmentJoystickBinding
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject

class JoystickFragment : Fragment() {

    private var _binding: FragmentJoystickBinding? = null
    private val binding get() = _binding!!

    // LiveData for joystick coordinates
    private val _joystickCoordinates = MutableLiveData<Pair<Float, Float>>()
    val joystickCoordinates: LiveData<Pair<Float, Float>> = _joystickCoordinates

    // Coroutine job to handle delayed updates
    private var updateJob: Job? = null

    // WebSocket instance
    private lateinit var webSocket: WebSocket

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentJoystickBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Connect to ROS bridge
        connectToRosBridge()

        // Set up the VirtualJoystickView
        val joystick = binding.virtualJoystick
        joystick.setOnMoveListener { x, y ->
            updateJoystickData(x, y)
        }

        // Observe LiveData to update UI
        joystickCoordinates.observe(viewLifecycleOwner) { coordinates ->
            val (x, y) = coordinates
            binding.textCoordinates.text = "X: ${"%.2f".format(x)}, Y: ${"%.2f".format(y)}"
            sendJoystickData(x, y)
        }

        return root
    }

    private fun connectToRosBridge() {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("ws://10.200.40.97:9090") // Update with your WebSocket URL
            .build()
        webSocket = client.newWebSocket(request, object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                Log.d("JoystickFragment", "WebSocket connected successfully")
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                Log.e("JoystickFragment", "WebSocket connection failed: ${t.message}", t)
                lifecycleScope.launch {
                    reconnectToRosBridge() // Attempt to reconnect
                }
            }
        })
    }

    private suspend fun reconnectToRosBridge() {
        delay(3000) // Wait 3 seconds before reconnecting
        Log.d("JoystickFragment", "Attempting to reconnect to ROS bridge...")
        connectToRosBridge()
    }

    private fun updateJoystickData(x: Float, y: Float) {
        // Cancel any existing job
        updateJob?.cancel()

        // Start a new coroutine to delay updates
        updateJob = lifecycleScope.launch {
            _joystickCoordinates.postValue(Pair(x, y))
            delay(100) // Limit updates to once every 100ms
        }
    }

    private fun sendJoystickData(x: Float, y: Float) {
        if (!::webSocket.isInitialized) {
            Log.e("JoystickFragment", "WebSocket is not initialized")
            return
        }

        // Construct the ROS2 message as a JSON object
        val message = JSONObject().apply {
            put("op", "publish")
            put("topic", "/joy")
            put("msg", JSONObject().apply {
                put("header", JSONObject().apply {
                    put("stamp", JSONObject().apply {
                        put("sec", System.currentTimeMillis() / 1000)
                        put("nanosec", (System.currentTimeMillis() % 1000) * 1_000_000)
                    })
                    put("frame_id", "")
                })
                put("axes", JSONArray(listOf(x, y))) // Properly format axes as a JSON array
                put("buttons", JSONArray(listOf(0, 0))) // Default buttons
            })
        }

        try {
            webSocket.send(message.toString())
            Log.d("JoystickFragment", "Joystick data sent: $message")
        } catch (e: Exception) {
            Log.e("JoystickFragment", "Failed to send joystick data", e)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        updateJob?.cancel() // Cancel the coroutine job to avoid memory leaks
        webSocket.close(1000, null) // Close the WebSocket connection
    }
}
