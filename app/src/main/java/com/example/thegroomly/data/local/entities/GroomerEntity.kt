package com.example.thegroomly.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "groomers")
data class GroomerEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val distanceMeters: Int,
    val photoRes: Int,
    val isFavorite: Boolean = false
)