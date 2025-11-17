package com.example.myapplication.network

// --- Data classes for parsing the Dictionary API JSON response ---

data class DictionaryApiResponse(
    val word: String,
    val meanings: List<Meaning>
)

data class Meaning(
    val partOfSpeech: String,
    val definitions: List<Definition>
)

data class Definition(
    val definition: String,
    val example: String?
)