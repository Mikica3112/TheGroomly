package com.example.thegroomly

data class Reservation(
    val groomer: String,
    val dogType: String,
    val treatment: String,
    val dateTime: String,
    val note: String?
)
