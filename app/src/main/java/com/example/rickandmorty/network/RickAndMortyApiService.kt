package com.example.rickandmorty.network

import com.example.rickandmorty.model.Character
import com.example.rickandmorty.model.CharacterPage
import com.example.rickandmorty.model.Episode
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/** Листинг 8.75: интерфейс сервиса с @GET и suspend fun. */
interface RickAndMortyApiService {
    @GET("character")
    suspend fun getCharacters(@Query("page") page: Int): CharacterPage

    @GET("character/{id}")
    suspend fun getCharacter(@Path("id") id: Int): Character

    @GET("episode/{id}")
    suspend fun getEpisode(@Path("id") id: Int): Episode

    /** Несколько id через запятую — один запрос вместо десятков. */
    @GET("episode/{ids}")
    suspend fun getEpisodes(@Path(value = "ids", encoded = true) ids: String): List<Episode>
}
