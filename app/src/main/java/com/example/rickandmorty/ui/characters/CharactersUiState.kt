package com.example.rickandmorty.ui.characters

import com.example.rickandmorty.model.Character

/** Листинг 8.102: отдельное состояние экрана «Персонажи». */
sealed interface CharactersUiState {
    data class Success(
        val characters: List<Character>,
        val isLoadingMore: Boolean = false,
        val hasMore: Boolean = true
    ) : CharactersUiState

    object Error : CharactersUiState
    object Loading : CharactersUiState
}
