package com.looker.ui_genre

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.looker.data_music.data.GenresRepository
import com.looker.domain_music.Genre
import com.looker.ui_genre.components.GenresCard
import kotlinx.coroutines.launch

@Composable
fun Genres(
	viewModel: GenresViewModel = viewModel(
		factory = GenresViewModelFactory(
			GenresRepository()
		)
	)
) {
	val context = LocalContext.current

	val scope = rememberCoroutineScope()

	val genresList by viewModel.genresList.collectAsState()

	LaunchedEffect(genresList) { launch { viewModel.getGenreList(context) } }

	GenresList(
		genresList = genresList,
		onGenreClick = {
			scope.launch { }
		}
	)
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun GenresList(
	genresList: List<Genre>,
	onGenreClick: (Genre) -> Unit
) {
	LazyVerticalGrid(columns = GridCells.Adaptive(200.dp)) {
		items(genresList) { genre ->
			GenresCard(genre = genre) {
				onGenreClick(genre)
			}
		}
	}
}