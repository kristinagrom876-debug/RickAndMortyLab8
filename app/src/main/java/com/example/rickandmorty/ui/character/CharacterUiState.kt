package com.example.rickandmorty.ui.character

import com.example.rickandmorty.model.Character
import com.example.rickandmorty.model.Episode

/** Отдельное состояние экрана «Персонаж». */
sealed interface CharacterUiState {
    data class Success(
        val character: Character,
        val episodes: List<Episode>
    ) : CharacterUiState

    object Error : CharacterUiState
    object Loading : CharacterUiState
}
