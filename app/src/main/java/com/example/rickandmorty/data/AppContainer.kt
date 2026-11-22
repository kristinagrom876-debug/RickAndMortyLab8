package com.example.rickandmorty.data

import com.example.rickandmorty.network.RickAndMortyApiService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

/** Листинг 8.80: контейнер зависимостей приложения. */
interface AppContainer {
    val charactersRepository: CharactersRepository
}

/** Листинг 8.81: BASE_URL, retrofit и retrofitService перенесены в контейнер. */
class DefaultAppContainer : AppContainer {
    private val baseUrl = "https://rickandmortyapi.com/api/"

    // Реальный API возвращает лишние поля (origin, location, url, created, characters).
    private val json = Json { ignoreUnknownKeys = true }

    private val retrofit: Retrofit = Retrofit.Builder()
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .baseUrl(baseUrl)
        .build()

    private val retrofitService: RickAndMortyApiService by lazy {
        retrofit.create(RickAndMortyApiService::class.java)
    }

    override val charactersRepository: CharactersRepository by lazy {
        NetworkCharactersRepository(retrofitService)
    }
}
