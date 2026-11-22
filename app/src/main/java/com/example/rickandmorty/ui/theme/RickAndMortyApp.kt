package com.example.rickandmorty.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.rickandmorty.ui.character.CharacterScreen
import com.example.rickandmorty.ui.character.CharacterViewModel
import com.example.rickandmorty.ui.characters.CharactersScreen
import com.example.rickandmorty.ui.characters.CharactersViewModel

/**
 * Переход между экранами без библиотеки Navigation: выбранный id хранится в состоянии,
 * у каждого экрана своя ViewModel и своя sealed-иерархия UiState.
 */
@Composable
fun RickAndMortyApp(modifier: Modifier = Modifier) {
    val charactersViewModel: CharactersViewModel = viewModel(factory = CharactersViewModel.Factory)
    val characterViewModel: CharacterViewModel = viewModel(factory = CharacterViewModel.Factory)

    var selectedCharacterId by remember { mutableStateOf<Int?>(null) }

    LaunchedEffect(selectedCharacterId) {
        selectedCharacterId?.let { characterViewModel.loadCharacter(it) }
    }

    if (selectedCharacterId == null) {
        CharactersScreen(
            charactersUiState = charactersViewModel.charactersUiState,
            retryAction = charactersViewModel::loadCharacters,
            loadNextPage = charactersViewModel::loadNextPage,
            onCharacterClick = { selectedCharacterId = it },
            modifier = modifier
        )
    } else {
        CharacterScreen(
            characterUiState = characterViewModel.characterUiState,
            retryAction = characterViewModel::retry,
            onBack = { selectedCharacterId = null },
            modifier = modifier
        )
    }
}
