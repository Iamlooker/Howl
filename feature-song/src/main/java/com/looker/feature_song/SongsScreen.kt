package com.looker.feature_song

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.looker.core_ui.songsList

@Composable
fun SongRoute(
	viewModel: SongsViewModel = hiltViewModel()
) {
	val songs by viewModel.songsState.collectAsState()
	LazyColumn(verticalArrangement = Arrangement.spacedBy(4.dp)) {
		songsList(songs = songs.songsState) {
			viewModel.playSong(it)
		}
	}
}