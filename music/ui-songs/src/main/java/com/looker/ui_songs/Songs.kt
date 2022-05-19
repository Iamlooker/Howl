package com.looker.ui_songs

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.looker.constants.Resource
import com.looker.core_model.Song
import com.looker.ui_songs.components.SongsCard

@Composable
fun Songs(
	songsList: Resource<List<Song>>,
	onSongClick: (Song) -> Unit,
) {
	Surface(color = MaterialTheme.colors.background) {
		when (songsList) {
			is Resource.Success -> SongsList(songsList = songsList.data, onSongClick = onSongClick)
			is Resource.Loading -> LoadingState()
			is Resource.Error -> Unit
		}
	}
}

@Composable
fun LoadingState() {
	Box(
		modifier = Modifier.fillMaxSize(),
		contentAlignment = Alignment.Center
	) {
		LinearProgressIndicator(Modifier.clip(CircleShape))
	}
}

@Composable
fun SongsList(
	modifier: Modifier = Modifier,
	songsList: List<Song>?,
	onSongClick: (Song) -> Unit = {},
) {
	LazyColumn(modifier = modifier) {
		items(songsList ?: emptyList()) { song ->
			SongsCard(Modifier.fillMaxWidth(), song) { onSongClick(song) }
		}
	}
}
