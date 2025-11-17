package com.example.myapplication.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profile")
data class User(
    @PrimaryKey
    val id: Int = 1, // Fixed ID for the single user profile
    val name: String = "",
    val phoneNumber: String = "",
    val email: String = ""
)