package com.looker.ui_songs

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.looker.constants.Resource
import com.looker.domain_music.Song
import com.looker.ui_songs.components.SongsCard

@Composable
fun Songs(
	songsList: Resource<List<Song>>,
	onSongClick: (Song) -> Unit,
) {
	Surface(color = MaterialTheme.colors.background) {
		when (songsList) {
			is Resource.Success -> {
				SongsList(
					songsList = songsList.data,
					onSongClick = onSongClick
				)
			}
			is Resource.Error -> Unit
			is Resource.Loading -> CircularProgressIndicator()
		}
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
