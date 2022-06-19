package com.looker.core_ui

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import com.looker.core_model.Song

fun LazyListScope.songsList(songs: SongUiState, onClick: (Song) -> Unit = {}) {
	when (songs) {
		is SongUiState.Success -> items(items = songs.songs, key = { it.mediaId }) {
			SongItem(onClick = { onClick(it) }, song = it)
		}
		SongUiState.Loading -> item { LoadingState() }
		SongUiState.Error -> item { Text("Error") }
	}
}

@Composable
fun SongsList(songs: SongUiState, onClick: (Song) -> Unit = {}) {
	when (songs) {
		is SongUiState.Success -> {
			songs.songs.forEach {
				SongItem(onClick = { onClick(it) }, song = it)
			}
		}
		SongUiState.Loading -> LoadingState()
		SongUiState.Error -> Text("Error")
	}
}

@Immutable
data class SongListUiState(
	val songsState: SongUiState
	)

sealed interface SongUiState {
	data class Success(val songs: List<Song>, val songsAreBlacklisted: Boolean = false) : SongUiState
	object Error : SongUiState
	object Loading : SongUiState
}