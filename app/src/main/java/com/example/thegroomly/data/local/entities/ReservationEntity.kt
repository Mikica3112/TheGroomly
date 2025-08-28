package com.example.thegroomly.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reservations")
data class ReservationEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val groomerId: Long,
    val dogType: String,
    val treatment: String,
    val dateTime: String,
    val note: String? = null
)