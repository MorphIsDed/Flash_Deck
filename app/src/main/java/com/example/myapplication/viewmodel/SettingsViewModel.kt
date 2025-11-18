package com.example.myapplication.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.PreferencesManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class SettingsViewModel(application: Application) : AndroidViewModel(application) {
    private val preferencesManager = PreferencesManager(application)

    // --- Options for UI ---
    val themes = listOf(
        "Tokyo Night" to "tokyo_night",
        "Blue Teal" to "blue_teal",
        "Purple" to "purple",
        "Green" to "green",
        "Orange" to "orange"
    )

    val fontSizes = listOf(
        "Small" to 1,
        "Default" to 0,
        "Large" to 2
    )

    // ✅ ADDED: Font Style options
    val fontStyles = listOf("Default", "Serif", "Monospace", "Cursive")

    // --- State Flows from Preferences ---
    val currentTheme: Flow<String> = preferencesManager.theme
    val darkMode: Flow<Boolean?> = preferencesManager.darkMode
    val fontSize: Flow<Int> = preferencesManager.fontSize
    val highContrast: Flow<Boolean> = preferencesManager.highContrast
    val reduceAnimations: Flow<Boolean> = preferencesManager.reduceAnimations
    val largeText: Flow<Boolean> = preferencesManager.largeText

    // ✅ ADDED: Font Style flow
    val fontStyle: Flow<String> = preferencesManager.fontStyle

    // --- Setters ---
    fun setTheme(theme: String) {
        viewModelScope.launch {
            preferencesManager.setTheme(theme)
        }
    }

    fun setDarkMode(enabled: Boolean?) {
        viewModelScope.launch {
            preferencesManager.setDarkMode(enabled)
        }
    }

    fun setFontSize(size: Int) {
        viewModelScope.launch {
            preferencesManager.setFontSize(size)
        }
    }

    fun setHighContrast(enabled: Boolean) {
        viewModelScope.launch {
            preferencesManager.setHighContrast(enabled)
        }
    }

    fun setReduceAnimations(enabled: Boolean) {
        viewModelScope.launch {
            preferencesManager.setReduceAnimations(enabled)
        }
    }

    fun setLargeText(enabled: Boolean) {
        viewModelScope.launch {
            preferencesManager.setLargeText(enabled)
        }
    }

    fun setFontStyle(style: String) {
        viewModelScope.launch {
            preferencesManager.setFontStyle(style)
        }
    }
}