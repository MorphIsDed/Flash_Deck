package com.example.myapplication.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.PreferencesManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SettingsViewModel(application: Application) : AndroidViewModel(application) {
    private val preferencesManager = PreferencesManager(application)

    // Theme options
    val themes = listOf(
        "Tokyo Night" to "tokyo_night",
        "Blue Teal" to "blue_teal",
        "Purple" to "purple",
        "Green" to "green",
        "Orange" to "orange"
    )

    // Font size options
    val fontSizes = listOf(
        "Small" to 1,
        "Default" to 0,
        "Large" to 2
    )

    // State flows
    val currentTheme = preferencesManager.theme
    val darkMode = preferencesManager.darkMode
    val fontSize = preferencesManager.fontSize
    val highContrast = preferencesManager.highContrast
    val reduceAnimations = preferencesManager.reduceAnimations
    val largeText = preferencesManager.largeText

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
}

