package com.example.myapplication.model

data class Item(
    val id: Int,
    val title: String,
    var temperature: Double, // Changed to var
    var position: String,    // Changed to var
    var velocity: Double     // Changed to var
)
