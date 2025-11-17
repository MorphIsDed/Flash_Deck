package com.example.myapplication.viewmodel

import android.app.Application
import android.speech.tts.TextToSpeech
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.AppDatabase
import com.example.myapplication.data.Card
import com.example.myapplication.data.Deck
import com.example.myapplication.network.RetrofitInstance
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Locale

class MainViewModel(application: Application) : AndroidViewModel(application) {

    // --- Database Setup ---
    private val db = AppDatabase.getDatabase(application)
    private val dao = db.dao()

    // --- UI States ---
    private val _allDecks = MutableStateFlow<List<Deck>>(emptyList())
    val allDecks: StateFlow<List<Deck>> = _allDecks.asStateFlow()

    private val _currentDeckCards = MutableStateFlow<List<Card>>(emptyList())
    val currentDeckCards: StateFlow<List<Card>> = _currentDeckCards.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // --- Init: Load Decks ---
    init {
        viewModelScope.launch {
            dao.getAllDecks().collect { decks ->
                _allDecks.value = decks
            }
        }
    }

    // --- Database Operations ---
    fun addDeck(name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.insertDeck(Deck(name = name))
        }
    }

    fun addCard(deckId: Int, front: String, back: String) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.insertCard(Card(deckId = deckId, frontText = front, backText = back))
            loadCardsForDeck(deckId)
        }
    }

    fun loadCardsForDeck(deckId: Int) {
        viewModelScope.launch {
            dao.getCardsForDeck(deckId).collect { cards ->
                _currentDeckCards.value = cards
            }
        }
    }

    // --- FEATURE 1: Dictionary API ---
    fun fetchDefinition(word: String, onResult: (String) -> Unit) {
        _isLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = RetrofitInstance.api.getDefinition(word)
                if (response.isNotEmpty() && response[0].meanings.isNotEmpty()) {
                    val def = response[0].meanings[0].definitions[0].definition
                    onResult(def)
                } else {
                    onResult("No definition found.")
                }
            } catch (e: Exception) {
                Log.e("API", "Error: ${e.message}")
                onResult("Error fetching definition.")
            } finally {
                _isLoading.value = false
            }
        }
    }

    // --- FEATURE 2: ML Kit Translation ---
    fun translateText(text: String, onResult: (String) -> Unit) {
        _isLoading.value = true

        val options = TranslatorOptions.Builder()
            .setSourceLanguage(TranslateLanguage.ENGLISH)
            .setTargetLanguage(TranslateLanguage.SPANISH)
            .build()

        val translator = Translation.getClient(options)

        val conditions = DownloadConditions.Builder().requireWifi().build()

        translator.downloadModelIfNeeded(conditions)
            .addOnSuccessListener {
                translator.translate(text)
                    .addOnSuccessListener { translated ->
                        onResult(translated)
                        _isLoading.value = false
                    }
                    .addOnFailureListener {
                        onResult("Translation Failed")
                        _isLoading.value = false
                    }
            }
            .addOnFailureListener {
                onResult("Model Download Failed")
                _isLoading.value = false
            }
    }

    // --- FEATURE 3: Text To Speech (THIS WAS MISSING) ---
    private var tts: TextToSpeech? = null

    fun initTTS() {
        tts = TextToSpeech(getApplication()) { status ->
            if (status == TextToSpeech.SUCCESS) {
                tts?.language = Locale.US
            }
        }
    }

    fun speak(text: String) {
        tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
    }
}