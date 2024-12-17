package com.example.myapplication.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.myapplication.databinding.FragmentItemDetailBinding

class ItemDetailFragment : Fragment() {

    private lateinit var binding: FragmentItemDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentItemDetailBinding.inflate(inflater, container, false)

        // Get the item data passed via arguments
        val itemTitleText = arguments?.getString("itemTitle") ?: "No Title"
        val itemIdText = arguments?.getString("itemId") ?: "No Id"
        val itemTemperatureText = arguments?.getString("itemTemperature") ?: "No Temperature"
        val itemPositionText = arguments?.getString("itemPosition") ?: "No Position"
        val itemVelocityText = arguments?.getString("itemVelocity") ?: "No Velocity"

        // Set the item data to the TextViews
        binding.itemTitle.text = itemTitleText
        binding.itemId.text = "Id: $itemIdText"
        binding.itemTemperature.text = "Temperature: $itemTemperatureText"
        binding.itemPosition.text = "Position: $itemPositionText"
        binding.itemVelocity.text = "Velocity: $itemVelocityText"

        return binding.root
    }
}