package com.example.myapplication.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.model.Item

class ItemAdapter(private val itemList: List<Item>, private val onClick: (Item) -> Unit) : RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {

    // ViewHolder class to hold references to the item views
    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.itemTitle)
        val id: TextView = itemView.findViewById(R.id.itemId)
        val temperature: TextView = itemView.findViewById(R.id.itemTemperature)
        val position: TextView = itemView.findViewById(R.id.itemPosition)
        val velocity: TextView = itemView.findViewById(R.id.itemVelocity)
    }

    // Create a new ViewHolder for each item
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_layout, parent, false)
        return ItemViewHolder(view)
    }

    // Bind data to the views in each item
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val currentItem = itemList[position]
        holder.title.text = currentItem.title
        holder.id.text = "Id: ${currentItem.id}"
        holder.temperature.text = "Temperature: ${currentItem.temperature}°C"
        holder.position.text = "Position: ${currentItem.position}"
        holder.velocity.text = "Velocity: ${currentItem.velocity} m/s"

        // Set up a click listener
        holder.itemView.setOnClickListener {
            onClick(currentItem)
        }
    }

    // Return the total number of items
    override fun getItemCount() = itemList.size
}