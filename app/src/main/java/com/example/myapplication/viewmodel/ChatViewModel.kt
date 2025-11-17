package com.example.myapplication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
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

    // ⚠️ REPLACE WITH YOUR ACTUAL API KEY
    private val apiKey = "AIzaSyA1KY4rZZacbjxr8jTw1Cg-qyg9x1gig_8"

    // Initialize Gemini (Flash is faster/cheaper for chat)
    private val generativeModel = GenerativeModel(
        modelName = "gemini-1.5-flash",
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

        // 1. Add User Message to List
        val currentList = _messages.value.toMutableList()
        currentList.add(ChatMessage(userMessage, true))
        _messages.value = currentList
        _isLoading.value = true

        viewModelScope.launch {
            try {
                // 2. Send to Gemini
                val response = generativeModel.generateContent(userMessage)

                // 3. Add AI Response to List
                val aiResponse = response.text ?: "I couldn't think of a response."
                currentList.add(ChatMessage(aiResponse, false))
                _messages.value = currentList // Update UI

            } catch (e: Exception) {
                currentList.add(ChatMessage("Error: ${e.localizedMessage}", false, true))
                _messages.value = currentList
            } finally {
                _isLoading.value = false
            }
        }
    }
}