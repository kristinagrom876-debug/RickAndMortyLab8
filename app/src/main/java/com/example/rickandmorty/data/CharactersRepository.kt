package com.example.rickandmorty.data

import com.example.rickandmorty.model.Character
import com.example.rickandmorty.model.CharacterPage
import com.example.rickandmorty.model.Episode
import com.example.rickandmorty.network.RickAndMortyApiService

/** Листинг 8.77: интерфейс репозитория с suspend-функциями. */
interface CharactersRepository {
    suspend fun getCharacters(page: Int): CharacterPage
    suspend fun getCharacter(id: Int): Character
    suspend fun getEpisode(id: Int): Episode
    suspend fun getEpisodes(ids: String): List<Episode>
}

/** Листинги 8.78, 8.82, 8.83: реализация получает apiService через конструктор. */
class NetworkCharactersRepository(
    private val rickAndMortyApiService: RickAndMortyApiService
) : CharactersRepository {
    override suspend fun getCharacters(page: Int): CharacterPage =
        rickAndMortyApiService.getCharacters(page)

    override suspend fun getCharacter(id: Int): Character =
        rickAndMortyApiService.getCharacter(id)

    override suspend fun getEpisode(id: Int): Episode =
        rickAndMortyApiService.getEpisode(id)

    override suspend fun getEpisodes(ids: String): List<Episode> =
        rickAndMortyApiService.getEpisodes(ids)
}
