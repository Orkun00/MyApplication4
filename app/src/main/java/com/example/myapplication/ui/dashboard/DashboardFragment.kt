package com.example.myapplication.ui.dashboard

import android.os.Bundle
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

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    private lateinit var itemAdapter: ItemAdapter
    private lateinit var dashboardViewModel: DashboardViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dashboardViewModel =
            ViewModelProvider(this).get(DashboardViewModel::class.java)

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Set up RecyclerView
        val recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Initialize the Adapter
        itemAdapter = ItemAdapter(emptyList()) { item ->
            // Handle item click by navigating to ItemDetailFragment
            val action = DashboardFragmentDirections
                .actionNavigationDashboardToItemDetailFragment(
                    item.title,
                    item.id.toString(),
                    item.temperature.toString(),
                    item.position,
                    item.velocity.toString()
                )
            findNavController().navigate(action)
        }

        // Set the Adapter to RecyclerView
        recyclerView.adapter = itemAdapter

        // Observe the LiveData from ViewModel and update the adapter's data
        dashboardViewModel.itemList.observe(viewLifecycleOwner) { items ->
            // Update adapter with the new data
            itemAdapter = ItemAdapter(items) { item ->
                val action = DashboardFragmentDirections
                    .actionNavigationDashboardToItemDetailFragment(
                        item.title,
                        item.id.toString(),
                        item.temperature.toString(),
                        item.position,
                        item.velocity.toString()
                    )
                findNavController().navigate(action)
            }
            recyclerView.adapter = itemAdapter
        }

        // Call the loadItems() method to load data
        dashboardViewModel.loadItems()

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
