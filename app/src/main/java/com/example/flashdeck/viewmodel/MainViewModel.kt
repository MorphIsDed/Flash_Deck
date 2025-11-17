package com.example.flashdeck.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// Simple data models used by the ViewModel
data class Deck(val id: Int, val name: String)
data class Card(val front: String, val back: String)

class MainViewModel : ViewModel() {
    private val _allDecks = MutableStateFlow<List<Deck>>(emptyList())
    val allDecks: StateFlow<List<Deck>> = _allDecks

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    // Simple in-memory storage for cards per deck
    private val cards = mutableMapOf<Int, MutableList<Card>>()

    // Auto-incrementing id generator for decks
    private var nextDeckId = 1

    fun addDeck(name: String) {
        val deck = Deck(nextDeckId++, name)
        _allDecks.update { current -> current + deck }
        cards[deck.id] = mutableListOf()
    }

    fun addCard(deckId: Int, front: String, back: String) {
        val list = cards.getOrPut(deckId) { mutableListOf() }
        list.add(Card(front, back))
    }

    /**
     * Simulated dictionary lookup — in a real app you'd call a repository/network API.
     * We mimic async work and return a fake definition via the callback.
     */
    fun fetchDefinition(word: String, callback: (String) -> Unit) {
        if (word.isBlank()) return
        viewModelScope.launch {
            _isLoading.value = true
            delay(400) // simulate latency
            callback("Definition of '$word'")
            _isLoading.value = false
        }
    }

    /** Simulated translation — returns the input reversed as a placeholder. */
    fun translateText(text: String, callback: (String) -> Unit) {
        if (text.isBlank()) return
        viewModelScope.launch {
            _isLoading.value = true
            delay(400)
            callback(text.reversed())
            _isLoading.value = false
        }
    }
}

