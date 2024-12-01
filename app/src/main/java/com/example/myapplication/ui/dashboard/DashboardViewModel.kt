package com.example.myapplication.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.model.Item

class DashboardViewModel : ViewModel() {

    // LiveData to observe the list of items
    private val _itemList = MutableLiveData<List<Item>>()
    val itemList: LiveData<List<Item>> = _itemList

    // Method to load items (for now, we'll use dummy data)
    fun loadItems() {
        val items = listOf(
            Item(1, "Motor 1", 23.5, "Kuzey", 12.0),
            Item(2, "Motor 2", 24.5, "Kuzey",12.0 ),
            Item(3, "Motor 3", 28.0, "Kuzey", 12.0),
            Item(4, "Motor 4", 26.4, "Kuzey", 12.0)
        )
        _itemList.value = items
    }
}
