package com.example.myapplication.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FlashcardDao {
    @Query("SELECT * FROM decks ORDER BY name ASC")
    fun getAllDecks(): Flow<List<Deck>>

    @Query("SELECT * FROM cards WHERE deckId = :deckId ORDER BY id ASC")
    fun getCardsForDeck(deckId: Int): Flow<List<Card>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDeck(deck: Deck)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCard(card: Card)
}