package com.example.rickandmorty.ui.characters

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.rickandmorty.R
import com.example.rickandmorty.model.Character
import com.example.rickandmorty.ui.speciesLabelRes
import com.example.rickandmorty.ui.theme.RickAndMortyTheme

/** Листинг 8.113: состояние экрана и действия передаются параметрами. */
@Composable
fun CharactersScreen(
    charactersUiState: CharactersUiState,
    retryAction: () -> Unit,
    loadNextPage: () -> Unit,
    onCharacterClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    when (charactersUiState) {
        is CharactersUiState.Loading -> LoadingScreen(modifier = modifier.fillMaxSize())
        is CharactersUiState.Error -> ErrorScreen(
            retryAction = retryAction,
            modifier = modifier.fillMaxSize()
        )
        is CharactersUiState.Success -> CharactersGridScreen(
            characters = charactersUiState.characters,
            isLoadingMore = charactersUiState.isLoadingMore,
            hasMore = charactersUiState.hasMore,
            loadNextPage = loadNextPage,
            onCharacterClick = onCharacterClick,
            modifier = modifier
        )
    }
}

/** Листинг 8.67: обычный статический vector, без animated-vector. */
@Composable
internal fun LoadingScreen(modifier: Modifier = Modifier) {
    Image(
        modifier = modifier.size(200.dp),
        painter = painterResource(R.drawable.loading_img),
        contentDescription = stringResource(R.string.loading)
    )
}

/** Листинги 8.68, 8.112: сообщение и кнопка повторной загрузки. */
@Composable
internal fun ErrorScreen(retryAction: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(R.drawable.ic_connection_error),
            contentDescription = stringResource(R.string.error_icon)
        )
        Text(
            text = stringResource(R.string.loading_failed),
            modifier = Modifier.padding(16.dp)
        )
        Button(onClick = retryAction) {
            Text(stringResource(R.string.retry))
        }
    }
}

/** Листинги 8.106–8.110, 8.111: адаптивная сетка карточек. */
@Composable
fun CharactersGridScreen(
    characters: List<Character>,
    isLoadingMore: Boolean,
    hasMore: Boolean,
    loadNextPage: () -> Unit,
    onCharacterClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(4.dp)
) {
    val gridState = rememberLazyGridState()
    val shouldLoadMore by remember {
        derivedStateOf {
            val lastVisible = gridState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: -1
            lastVisible >= gridState.layoutInfo.totalItemsCount - 4
        }
    }
    LaunchedEffect(shouldLoadMore, characters.size) {
        if (shouldLoadMore && hasMore && !isLoadingMore) loadNextPage()
    }

    Column(modifier = modifier.fillMaxSize()) {
        Text(
            text = stringResource(R.string.characters_title),
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(8.dp)
        )
        LazyVerticalGrid(
            columns = GridCells.Adaptive(150.dp),
            state = gridState,
            modifier = Modifier
                .padding(horizontal = 4.dp)
                .weight(1f),
            contentPadding = contentPadding
        ) {
            items(items = characters, key = { character -> character.id }) { character ->
                CharacterCard(
                    character = character,
                    modifier = Modifier
                        .padding(4.dp)
                        .fillMaxWidth(),
                    onClick = { onCharacterClick(character.id) }
                )
            }
            if (isLoadingMore) {
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CircularProgressIndicator(modifier = Modifier.size(24.dp))
                        Spacer(Modifier.width(8.dp))
                        Text(stringResource(R.string.loading_more))
                    }
                }
            }
        }
    }
}

/** Листинги 8.99, 8.104, 8.105, 8.109: Card + AsyncImage с заглушками. */
@Composable
private fun CharacterCard(
    character: Character,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier.clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
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
                .aspectRatio(1f)
        )
        Text(
            text = character.name,
            modifier = Modifier.padding(horizontal = 4.dp),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            text = stringResource(speciesLabelRes(character.species)),
            modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CharactersGridScreenPreview() {
    RickAndMortyTheme {
        val mockData = listOf(
            Character(
                id = 1,
                name = "Rick Sanchez",
                status = "Alive",
                species = "Human",
                type = "",
                gender = "Male",
                image = "",
                episode = emptyList()
            ),
            Character(
                id = 2,
                name = "Morty Smith",
                status = "Alive",
                species = "Human",
                type = "",
                gender = "Male",
                image = "",
                episode = emptyList()
            )
        )
        CharactersGridScreen(
            characters = mockData,
            isLoadingMore = false,
            hasMore = true,
            loadNextPage = {},
            onCharacterClick = {}
        )
    }
}
