package com.example.myapplication.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FlashcardDao {
    // Deck Queries
    @Query("SELECT * FROM decks ORDER BY name ASC")
    fun getAllDecks(): Flow<List<Deck>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDeck(deck: Deck)

    @Query("UPDATE decks SET score = :score WHERE id = :deckId")
    suspend fun updateDeckScore(deckId: Int, score: Int)

    // Card Queries
    @Query("SELECT * FROM cards WHERE deckId = :deckId ORDER BY id ASC")
    fun getCardsForDeck(deckId: Int): Flow<List<Card>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCard(card: Card)

    // User Profile Queries
    @Query("SELECT * FROM user_profile WHERE id = 1")
    fun getUserProfile(): Flow<User?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateUserProfile(user: User)
}