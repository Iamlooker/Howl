package com.looker.ui_genre

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.ImageLoader
import com.looker.components.HowlSurface
import com.looker.data_music.data.GenresRepository
import com.looker.domain_music.Genre
import com.looker.ui_genre.components.GenresCard
import kotlinx.coroutines.launch

@Composable
fun Genres(imageLoader: ImageLoader) {
    Genres(modifier = Modifier.fillMaxSize(), imageLoader = imageLoader)
}

@Composable
private fun Genres(
    modifier: Modifier = Modifier,
    viewModel: GenresViewModel = viewModel(
        factory = GenresViewModelFactory(
            GenresRepository()
        )
    ),
    imageLoader: ImageLoader
) {
    HowlSurface(modifier) {

        val context = LocalContext.current

        val scope = rememberCoroutineScope()

        val genresList by viewModel.genresList.observeAsState(listOf())

        LaunchedEffect(genresList) { launch { viewModel.getGenreList(context) } }

        GenresList(
            genresList = genresList,
            imageLoader = imageLoader,
            onGenreClick = {
                scope.launch { }
            }
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun GenresList(
    genresList: List<Genre>,
    imageLoader: ImageLoader,
    onGenreClick: (Genre) -> Unit
) {
    LazyVerticalGrid(cells = GridCells.Adaptive(200.dp)) {
        items(genresList) { genre ->
            GenresCard(genre = genre, imageLoader = imageLoader) {
                onGenreClick(genre)
            }
        }
    }
}