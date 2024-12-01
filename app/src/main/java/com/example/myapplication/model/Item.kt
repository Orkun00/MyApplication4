package com.example.myapplication.model

data class Item(
    val id: Int,
    val title: String,
    val temperature: Double,
    val position: String,  // You can change this to Double if needed
    val velocity: Double
)
