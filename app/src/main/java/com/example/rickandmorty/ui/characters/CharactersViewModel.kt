package com.example.rickandmorty.ui.characters

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
import kotlinx.coroutines.launch

/** Состояние пагинации (page / hasMore / isLoading) хранится в ViewModel. */
class CharactersViewModel(
    private val charactersRepository: CharactersRepository
) : ViewModel() {

    var charactersUiState: CharactersUiState by mutableStateOf(CharactersUiState.Loading)
        private set

    private var currentPage = 1
    private var hasMore = true
    private var isLoading = false

    init {
        loadCharacters()
    }

    /** Первая страница; используется и как retryAction (листинг 8.114). */
    fun loadCharacters() {
        if (isLoading) return
        currentPage = 1
        hasMore = true
        isLoading = true
        charactersUiState = CharactersUiState.Loading

        viewModelScope.launch {
            charactersUiState = try {
                val page = charactersRepository.getCharacters(currentPage)
                hasMore = page.info.next != null
                CharactersUiState.Success(characters = page.results, hasMore = hasMore)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                CharactersUiState.Error
            } finally {
                isLoading = false
            }
        }
    }

    /** Подгрузка следующей страницы при приближении к концу списка. */
    fun loadNextPage() {
        if (isLoading || !hasMore) return
        val state = charactersUiState
        if (state !is CharactersUiState.Success || state.isLoadingMore) return

        val nextPage = currentPage + 1
        isLoading = true
        charactersUiState = state.copy(isLoadingMore = true)

        viewModelScope.launch {
            charactersUiState = try {
                val page = charactersRepository.getCharacters(nextPage)
                currentPage = nextPage
                hasMore = page.info.next != null
                CharactersUiState.Success(
                    characters = state.characters + page.results,
                    hasMore = hasMore
                )
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                // Оставшиеся данные показываем, но дальше не повторяем запросы.
                hasMore = false
                CharactersUiState.Success(characters = state.characters, hasMore = false)
            } finally {
                isLoading = false
            }
        }
    }

    /** Листинг 8.87: фабрика ViewModel с репозиторием из AppContainer. */
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as RickAndMortyApplication)
                val charactersRepository = application.container.charactersRepository
                CharactersViewModel(charactersRepository = charactersRepository)
            }
        }
    }
}
