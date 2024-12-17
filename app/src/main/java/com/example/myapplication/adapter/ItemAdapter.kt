package com.example.myapplication.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.ItemLayoutBinding
import com.example.myapplication.model.Item

class ItemAdapter(private val onClick: (Item) -> Unit) : ListAdapter<Item, ItemAdapter.ItemViewHolder>(ItemDiffCallback()) {

    // ViewHolder class to bind data using DataBinding
    class ItemViewHolder(private val binding: ItemLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        // Binding data directly to views in layout
        fun bind(item: Item) {
            binding.item = item
            binding.executePendingBindings()
        }
    }

    // DiffUtil.ItemCallback to calculate differences between old and new list
    class ItemDiffCallback : DiffUtil.ItemCallback<Item>() {
        override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean {
            return oldItem.id == newItem.id // Unique identifier to check if items are the same
        }

        override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean {
            return oldItem == newItem // Check if the content of the items are the same
        }
    }

    // Create a new ViewHolder for each item
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = ItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    // Bind data to the views in each item
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)

        // Set up a click listener
        holder.itemView.setOnClickListener {
            onClick(currentItem)
        }
    }
}