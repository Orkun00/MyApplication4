package com.example.myapplication.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.adapter.ItemAdapter
import com.example.myapplication.databinding.FragmentDashboardBinding

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    private lateinit var itemAdapter: ItemAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val dashboardViewModel =
            ViewModelProvider(this).get(DashboardViewModel::class.java)

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Set up RecyclerView
        val recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
//
        // Initialize the Adapter
        itemAdapter = ItemAdapter(emptyList()) { item ->
            // Handle item click, for example, show a Toast or navigate to a detail screen
            Toast.makeText(requireContext(), "Clicked on: ${item.title}", Toast.LENGTH_SHORT).show()
        }

        // Set the Adapter to RecyclerView
        recyclerView.adapter = itemAdapter

        // Observe the LiveData from ViewModel and update the adapter's data
        dashboardViewModel.itemList.observe(viewLifecycleOwner) { items ->
            itemAdapter = ItemAdapter(items) { item ->
                // Handle item click, for example, show a Toast or navigate to a detail screen
//                Toast.makeText(requireContext(), "Clicked on: ${item.title}", Toast.LENGTH_SHORT).show()
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
