package com.looker.ui_songs

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.looker.domain_music.Song
import com.looker.ui_songs.components.SongsCard

@Composable
fun Songs(
	songsList: List<Song>,
	onSongClick: (Int) -> Unit,
) {
	Surface(color = MaterialTheme.colors.background) {
		SongsList(
			songsList = songsList,
			onSongClick = onSongClick
		)
	}
}

@Composable
fun SongsList(
	modifier: Modifier = Modifier,
	songsList: List<Song>,
	onSongClick: (Int) -> Unit = {},
) {
	LazyColumn(modifier = modifier) {
		itemsIndexed(songsList) { index, song ->
			SongsCard(Modifier.fillMaxWidth(), song) { onSongClick(index) }
		}
	}
}
