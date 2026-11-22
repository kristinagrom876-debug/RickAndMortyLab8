package com.example.rickandmorty.ui.character

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.rickandmorty.R
import com.example.rickandmorty.model.Character
import com.example.rickandmorty.model.Episode
import com.example.rickandmorty.ui.characters.ErrorScreen
import com.example.rickandmorty.ui.characters.LoadingScreen
import com.example.rickandmorty.ui.genderLabelRes
import com.example.rickandmorty.ui.localizedAirDate
import com.example.rickandmorty.ui.speciesLabelRes
import com.example.rickandmorty.ui.statusLabelRes
import com.example.rickandmorty.ui.theme.RickAndMortyTheme
import com.example.rickandmorty.ui.typeLabelRes

/** Второй экран: обычные строки без кастомных карточек и оформления. */
@Composable
fun CharacterScreen(
    characterUiState: CharacterUiState,
    retryAction: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxSize()) {
        Text(
            text = stringResource(R.string.character_title),
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(8.dp)
        )
        Button(onClick = onBack, modifier = Modifier.padding(horizontal = 8.dp)) {
            Text(stringResource(R.string.back))
        }
        when (characterUiState) {
            is CharacterUiState.Loading -> LoadingScreen(modifier = Modifier.weight(1f))
            is CharacterUiState.Error -> ErrorScreen(
                retryAction = retryAction,
                modifier = Modifier.weight(1f)
            )
            is CharacterUiState.Success -> CharacterDetails(
                character = characterUiState.character,
                episodes = characterUiState.episodes,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun CharacterDetails(
    character: Character,
    episodes: List<Episode>,
    modifier: Modifier = Modifier
) {
    val resources = LocalContext.current.resources
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(8.dp)
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(character.image)
                .crossfade(true)
                .build(),
            contentDescription = stringResource(R.string.character_photo),
            contentScale = ContentScale.Fit,
            error = painterResource(R.drawable.ic_broken_image),
            placeholder = painterResource(R.drawable.loading_img),
            modifier = Modifier
                .fillMaxWidth()
                .height(280.dp)
        )
        Text(
            text = stringResource(R.string.name_label, character.name),
            modifier = Modifier.padding(top = 8.dp)
        )
        Text(
            text = stringResource(
                R.string.character_type,
                stringResource(typeLabelRes(character.type))
            )
        )
        Text(
            text = stringResource(
                R.string.character_status,
                stringResource(statusLabelRes(character.status))
            )
        )
        Text(
            text = stringResource(
                R.string.character_species,
                stringResource(speciesLabelRes(character.species))
            )
        )
        Text(
            text = stringResource(
                R.string.character_gender,
                stringResource(genderLabelRes(character.gender))
            )
        )
        Text(
            text = stringResource(R.string.episodes_title),
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(top = 16.dp, bottom = 4.dp)
        )
        episodes.forEach { episode ->
            Text(
                text = stringResource(
                    R.string.episode_line,
                    episode.episode,
                    localizedAirDate(episode.airDate, resources)
                ),
                modifier = Modifier.padding(vertical = 2.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CharacterScreenPreview() {
    RickAndMortyTheme {
        CharacterScreen(
            characterUiState = CharacterUiState.Success(
                character = Character(
                    id = 1,
                    name = "Rick Sanchez",
                    status = "Alive",
                    species = "Human",
                    type = "",
                    gender = "Male",
                    image = "",
                    episode = listOf("https://rickandmortyapi.com/api/episode/1")
                ),
                episodes = listOf(
                    Episode(
                        id = 1,
                        name = "Pilot",
                        airDate = "December 2, 2013",
                        episode = "S01E01"
                    )
                )
            ),
            retryAction = {},
            onBack = {}
        )
    }
}
