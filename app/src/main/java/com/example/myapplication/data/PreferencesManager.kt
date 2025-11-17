package com.example.myapplication.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class PreferencesManager(context: Context) {
    private val dataStore = context.dataStore

    companion object {
        val THEME_KEY = stringPreferencesKey("theme")
        val DARK_MODE_KEY = booleanPreferencesKey("dark_mode")
        val FONT_SIZE_KEY = intPreferencesKey("font_size")
        val HIGH_CONTRAST_KEY = booleanPreferencesKey("high_contrast")
        val REDUCE_ANIMATIONS_KEY = booleanPreferencesKey("reduce_animations")
        val LARGE_TEXT_KEY = booleanPreferencesKey("large_text")
    }

    val theme: Flow<String> = dataStore.data.map { preferences ->
        preferences[THEME_KEY] ?: "tokyo_night"
    }

    val darkMode: Flow<Boolean?> = dataStore.data.map { preferences ->
        preferences[DARK_MODE_KEY]
    }

    val fontSize: Flow<Int> = dataStore.data.map { preferences ->
        preferences[FONT_SIZE_KEY] ?: 0 // 0 = default, 1 = small, 2 = large
    }

    val highContrast: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[HIGH_CONTRAST_KEY] ?: false
    }

    val reduceAnimations: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[REDUCE_ANIMATIONS_KEY] ?: false
    }

    val largeText: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[LARGE_TEXT_KEY] ?: false
    }

    suspend fun setTheme(theme: String) {
        dataStore.edit { preferences ->
            preferences[THEME_KEY] = theme
        }
    }

    suspend fun setDarkMode(enabled: Boolean?) {
        dataStore.edit { preferences ->
            if (enabled != null) {
                preferences[DARK_MODE_KEY] = enabled
            } else {
                preferences.remove(DARK_MODE_KEY)
            }
        }
    }

    suspend fun setFontSize(size: Int) {
        dataStore.edit { preferences ->
            preferences[FONT_SIZE_KEY] = size
        }
    }

    suspend fun setHighContrast(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[HIGH_CONTRAST_KEY] = enabled
        }
    }

    suspend fun setReduceAnimations(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[REDUCE_ANIMATIONS_KEY] = enabled
        }
    }

    suspend fun setLargeText(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[LARGE_TEXT_KEY] = enabled
        }
    }
}

