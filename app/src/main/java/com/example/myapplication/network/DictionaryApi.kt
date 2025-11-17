package com.example.myapplication.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

// --- 1. Data Classes (JSON Model) ---

data class WordResult(
    val meanings: List<Meaning>
)

data class Meaning(
    val partOfSpeech: String,
    val definitions: List<DefinitionDetail>
)

data class DefinitionDetail(
    val definition: String
)

// --- 2. API Interface ---

interface DictionaryApi {
    @GET("entries/en/{word}")
    suspend fun getDefinition(@Path("word") word: String): List<WordResult>
}

// --- 3. Singleton Instance ---

object RetrofitInstance {
    private const val BASE_URL = "https://api.dictionaryapi.dev/api/v2/"

    val api: DictionaryApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(DictionaryApi::class.java)
    }
}