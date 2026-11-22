package com.example.rickandmorty.ui.character

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.rickandmorty.RickAndMortyApplication
import com.example.rickandmorty.data.CharactersRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class CharacterViewModel(
    private val charactersRepository: CharactersRepository
) : ViewModel() {

    var characterUiState: CharacterUiState by mutableStateOf(CharacterUiState.Loading)
        private set

    private var currentCharacterId: Int? = null

    fun loadCharacter(characterId: Int) {
        currentCharacterId = characterId
        characterUiState = CharacterUiState.Loading

        viewModelScope.launch {
            characterUiState = try {
                coroutineScope {
                    // Листинги 8.43-8.44: async + awaitAll; порции обрабатываются последовательно.
                    val character = async { charactersRepository.getCharacter(characterId) }.await()
                    val episodeIds = character.episode
                        .map { url -> url.substringAfterLast('/') }
                    val episodes = episodeIds.chunked(10).map { chunk ->
                        async {
                            // Один id API возвращает объектом, а не массивом.
                            if (chunk.size == 1) {
                                listOf(charactersRepository.getEpisode(chunk[0].toInt()))
                            } else {
                                charactersRepository.getEpisodes(chunk.joinToString(","))
                            }
                        }
                    }.awaitAll().flatten()
                    CharacterUiState.Success(character = character, episodes = episodes)
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                CharacterUiState.Error
            }
        }
    }

    /** Аналог retryAction для второго экрана. */
    fun retry() {
        currentCharacterId?.let { loadCharacter(it) }
    }

    /** Листинг 8.87: фабрика ViewModel с репозиторием из AppContainer. */
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as RickAndMortyApplication)
                val charactersRepository = application.container.charactersRepository
                CharacterViewModel(charactersRepository = charactersRepository)
            }
        }
    }
}
