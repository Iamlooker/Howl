package com.looker.feature_song

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.looker.core_ui.components.SongUiState
import com.looker.core_ui.components.songsList
import com.looker.feature_song.components.OrderChips

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SongRoute(
	viewModel: SongsViewModel = hiltViewModel()
) {
	val songs by viewModel.songsState.collectAsState()
	LazyColumn(verticalArrangement = Arrangement.spacedBy(4.dp)) {
		stickyHeader {
			if (songs.songsState is SongUiState.Success) {
				OrderChips(order = (songs.songsState as SongUiState.Success).songOrder) { songOrder ->
					viewModel.onEvent(SongEvent.Order(songOrder))
				}
			}
		}
		songsList(songs = songs.songsState) {
			viewModel.onEvent(SongEvent.PlayMedia(it))
		}
	}
}