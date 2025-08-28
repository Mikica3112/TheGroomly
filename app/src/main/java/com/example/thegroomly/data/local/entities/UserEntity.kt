package com.example.thegroomly.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val id: Int = 1,   // Jedan korisnik u appu
    val name: String = "",
    val email: String = "",
    val phone: String = "",
    val address: String = ""
)
