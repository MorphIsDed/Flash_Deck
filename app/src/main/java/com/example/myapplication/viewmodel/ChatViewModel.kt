package com.example.myapplication.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.ai.client.generativeai.GenerativeModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ChatMessage(
    val text: String,
    val isUser: Boolean,
    val isError: Boolean = false
)

class ChatViewModel : ViewModel() {

    // 🛑 STOP: DID YOU PASTE YOUR KEY HERE? 🛑
    private val apiKey = "YOUR_API_KEY_HERE"

    // ✅ FIXED: Using 'gemini-pro' because it is the most stable model.
    // If this works, you can try 'gemini-1.5-flash-001' later.
    private val generativeModel = GenerativeModel(
        modelName = "gemini-pro",
        apiKey = apiKey
    )

    private val _messages = MutableStateFlow<List<ChatMessage>>(
        listOf(ChatMessage("Hi! I'm your AI Study Buddy. Ask me about your flashcards!", false))
    )
    val messages: StateFlow<List<ChatMessage>> = _messages.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun sendMessage(userMessage: String) {
        if (userMessage.isBlank()) return

        // 1. Add User Message
        val currentList = _messages.value.toMutableList()
        currentList.add(ChatMessage(userMessage, true))
        _messages.value = currentList
        _isLoading.value = true

        viewModelScope.launch {
            try {
                if (apiKey == "AIzaSyA1KY4rZZacbjxr8jTw1Cg-qyg9x1gig_8" || apiKey.isBlank()) {
                    throw Exception("API Key is missing! Check ChatViewModel.")
                }

                // 2. Send to Gemini
                val response = generativeModel.generateContent(userMessage)

                // 3. Add AI Response
                // We use safe call ?.text to prevent crashes if response is null
                val aiResponse = response.text ?: "I couldn't generate a response."

                val newList = _messages.value.toMutableList()
                newList.add(ChatMessage(aiResponse, false))
                _messages.value = newList

            } catch (e: Exception) {
                // Log the error so you can see it in Logcat
                Log.e("ChatViewModel", "Error sending message", e)

                val errorList = _messages.value.toMutableList()
                // Show a user-friendly error bubble
                errorList.add(ChatMessage("Error: ${e.message ?: "Unknown error"}", false, true))
                _messages.value = errorList
            } finally {
                _isLoading.value = false
            }
        }
    }
}