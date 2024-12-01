package com.example.myapplication.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.myapplication.R
class ItemDetailFragment : Fragment() {

    private lateinit var itemTitle: TextView
    private lateinit var itemTemperature: TextView
    private lateinit var itemPosition: TextView
    private lateinit var itemVelocity: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root: View = inflater.inflate(R.layout.fragment_item_detail, container, false)

        itemTitle = root.findViewById(R.id.itemTitle)
        itemTemperature = root.findViewById(R.id.itemTemperature)
        itemPosition = root.findViewById(R.id.itemPosition)
        itemVelocity = root.findViewById(R.id.itemVelocity)

        // Get the item data passed via arguments
        val itemTitleText = arguments?.getString("itemTitle") ?: "No Title"
        val itemTemperatureText = arguments?.getString("itemTemperature") ?: "No Temperature"
        val itemPositionText = arguments?.getString("itemPosition") ?: "No Position"
        val itemVelocityText = arguments?.getString("itemVelocity") ?: "No Velocity"

        // Set the item data to the TextViews
        itemTitle.text = itemTitleText
        itemTemperature.text = "Temperature: $itemTemperatureText"
        itemPosition.text = "Position: $itemPositionText"
        itemVelocity.text = "Velocity: $itemVelocityText"

        return root
    }
}
