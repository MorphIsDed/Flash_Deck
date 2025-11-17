package com.example.myapplication.viewmodel

import android.app.Application
import android.speech.tts.TextToSpeech
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.AppDatabase
import com.example.myapplication.data.Card
import com.example.myapplication.data.Deck
// ✅ ADDED THIS IMPORT:
import com.example.myapplication.network.RetrofitInstance
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.languageid.LanguageIdentification
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Locale
import kotlin.math.min

// Simple data class for tracking review stats in memory
data class CardStats(
    val cardId: Int,
    val timesReviewed: Int = 0,
    val correctCount: Int = 0,
    val nextReviewTime: Long = System.currentTimeMillis(),
    val confidence: Float = 0f // 0-1 scale
)

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

    // --- AI Features ---
    private val _detectedLanguage = MutableStateFlow("en")
    val detectedLanguage: StateFlow<String> = _detectedLanguage.asStateFlow()

    private val _cardStats = MutableStateFlow<Map<Int, CardStats>>(emptyMap())
    val cardStats: StateFlow<Map<Int, CardStats>> = _cardStats.asStateFlow()

    // --- Language Identification Client ---
    private val languageId = LanguageIdentification.getClient()

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

    // --- FEATURE 1: Dictionary API with Enhanced UI ---
    fun fetchDefinition(word: String, onResult: (String) -> Unit) {
        if (word.isBlank()) {
            onResult("Please enter a word")
            return
        }
        _isLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = RetrofitInstance.api.getDefinition(word)
                if (response.isNotEmpty() && response[0].meanings.isNotEmpty()) {
                    val def = response[0].meanings[0].definitions[0].definition
                    val partOfSpeech = response[0].meanings[0].partOfSpeech
                    onResult("📚 $partOfSpeech: $def")
                } else {
                    onResult("❌ No definition found for '$word'")
                }
            } catch (e: Exception) {
                Log.e("API", "Error: ${e.message}")
                onResult("⚠️ Error fetching definition.")
            } finally {
                _isLoading.value = false
            }
        }
    }

    // --- FEATURE 2: Smart Language Detection & Translation ---
    fun detectLanguageAndTranslate(text: String, onResult: (String) -> Unit) {
        if (text.isBlank()) {
            onResult("Please enter text to translate")
            return
        }

        _isLoading.value = true
        languageId.identifyLanguage(text)
            .addOnSuccessListener { languageCode ->
                Log.d("LangID", "Detected: $languageCode")
                _detectedLanguage.value = languageCode

                // Logic: If detected Spanish, translate to English. Otherwise, translate to Spanish.
                val targetLang = if (languageCode == "es") {
                    TranslateLanguage.ENGLISH
                } else {
                    TranslateLanguage.SPANISH
                }

                translateToLanguage(text, targetLang, onResult)
            }
            .addOnFailureListener { e ->
                Log.e("LangID", "Detection failed: ${e.message}")
                // Fallback to English->Spanish
                translateToLanguage(text, TranslateLanguage.SPANISH, onResult)
            }
    }

    private fun translateToLanguage(
        text: String,
        targetLang: String,
        onResult: (String) -> Unit
    ) {
        val options = TranslatorOptions.Builder()
            .setSourceLanguage(TranslateLanguage.ENGLISH) // Default Source
            .setTargetLanguage(targetLang)
            .build()

        val translator = Translation.getClient(options)
        val conditions = DownloadConditions.Builder().requireWifi().build()

        translator.downloadModelIfNeeded(conditions)
            .addOnSuccessListener {
                translator.translate(text)
                    .addOnSuccessListener { translated ->
                        onResult("🌐 $translated")
                        _isLoading.value = false
                    }
                    .addOnFailureListener { e ->
                        onResult("❌ Translation failed")
                        _isLoading.value = false
                    }
            }
            .addOnFailureListener { e ->
                onResult("⚠️ Model download failed")
                _isLoading.value = false
            }
    }

    // --- FEATURE 3: Spaced Repetition Algorithm ---
    fun recordCardReview(cardId: Int, isCorrect: Boolean) {
        val currentStats = _cardStats.value[cardId] ?: CardStats(cardId)

        // Update confidence score based on correctness
        val newConfidence = if (isCorrect) {
            min(currentStats.confidence + 0.1f, 1f)
        } else {
            (currentStats.confidence - 0.15f).coerceAtLeast(0f)
        }

        // Calculate next review time (Simple Spaced Repetition)
        val interval = when (currentStats.timesReviewed) {
            0 -> 1 * 24 * 60 * 60 * 1000L  // 1 day
            1 -> 3 * 24 * 60 * 60 * 1000L  // 3 days
            2 -> 7 * 24 * 60 * 60 * 1000L  // 7 days
            else -> 14 * 24 * 60 * 60 * 1000L // 14 days
        }

        val nextReview = System.currentTimeMillis() + interval

        val newStats = currentStats.copy(
            timesReviewed = currentStats.timesReviewed + 1,
            correctCount = if (isCorrect) currentStats.correctCount + 1 else currentStats.correctCount,
            nextReviewTime = nextReview,
            confidence = newConfidence
        )

        _cardStats.value = _cardStats.value.toMutableMap().apply {
            put(cardId, newStats)
        }

        Log.d("SpacedRep", "Card $cardId Confidence: $newConfidence")
    }

    // --- FEATURE 4: Text To Speech ---
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

    override fun onCleared() {
        super.onCleared()
        tts?.shutdown()
    }
}