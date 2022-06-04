package com.looker.feature_song

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.looker.core_model.Song
import com.looker.core_ui.LoadingState
import com.looker.core_ui.SongItem

@Composable
fun SongRoute(
	viewModel: SongsViewModel = hiltViewModel()
) {
	val songs by viewModel.songsState.collectAsState()
	LazyColumn { songsList(songs = songs.songsState) { viewModel.playSong(it) } }
}

private fun LazyListScope.songsList(songs: SongUiState, onClick: (Song) -> Unit = {}) {
	when (songs) {
		is SongUiState.Success -> items(songs.songs) {
			SongItem(onClick = { onClick(it) }, song = it)
		}
		SongUiState.Loading -> item {
			LoadingState()
		}
		SongUiState.Error -> item { Text("Error") }
	}
}