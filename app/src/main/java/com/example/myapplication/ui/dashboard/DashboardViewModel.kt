package com.example.myapplication.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.model.Item

class DashboardViewModel : ViewModel() {

    // LiveData to observe the list of items
    private val _itemList = MutableLiveData<List<Item>>()
    val itemList: LiveData<List<Item>> = _itemList

    // Map to store motor data dynamically
    private val motorData = mutableMapOf(
        "motor1" to Item(1, "Motor 1", 0.0, "0.0", 0.0),
        "motor2" to Item(2, "Motor 2", 0.0, "0.0", 0.0),
        "motor3" to Item(3, "Motor 3", 0.0, "0.0", 0.0),
        "motor4" to Item(4, "Motor 4", 0.0, "0.0", 0.0)
    )

    init {
        // Ensure initial data is available
        updateItemList()
    }

    // Update the motor data dynamically
    fun updateMotorData(topic: String, value: Double) {
        val parts = topic.split("/")
        if (parts.size == 2) {
            val motor = parts[0] // e.g., motor1, motor2
            val field = parts[1] // e.g., temp, pos, vel

            motorData[motor]?.let {
                when (field) {
                    "temp" -> it.temperature = value
                    "pos" -> it.position = value.toString()
                    "vel" -> it.velocity = value
                }
            }
            updateItemList()
        }
    }

    // Update LiveData to refresh the RecyclerView
    private fun updateItemList() {
        _itemList.value = motorData.values.toList()
    }
}

