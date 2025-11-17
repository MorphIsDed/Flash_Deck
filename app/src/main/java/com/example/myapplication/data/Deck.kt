package com.example.myapplication.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "decks")
data class Deck(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    // Score from the last review session, stored as a percentage (0-100).
    // -1 indicates no score has been recorded yet.
    val score: Int = -1
)