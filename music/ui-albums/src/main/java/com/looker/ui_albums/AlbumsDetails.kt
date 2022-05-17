package com.looker.ui_albums

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.looker.components.ext.backgroundGradient
import com.looker.components.localComposers.LocalDurations
import com.looker.core_model.Album
import com.looker.core_model.Song
import com.looker.ui_albums.components.AlbumsDetailsItem
import com.looker.ui_songs.SongsList

@Composable
fun AlbumsBottomSheetContent(
	modifier: Modifier = Modifier,
	currentAlbum: Album?,
	songsList: List<Song>,
	dominantColor: Color = MaterialTheme.colors.surface,
	onSongClick: (Song) -> Unit
) {
	AlbumBottomSheetItem(
		modifier = modifier,
		album = currentAlbum,
		albumDominantColor = dominantColor,
		songsList = songsList,
		onSongClick = onSongClick
	)
}

@Composable
fun AlbumBottomSheetItem(
	modifier: Modifier = Modifier,
	album: Album?,
	albumDominantColor: Color,
	songsList: List<Song>,
	onSongClick: (Song) -> Unit
) {
	Column(modifier = modifier.backgroundGradient(albumDominantColor)) {
		AlbumHeader(album = album)
		AlbumSongsList(songsList = songsList, onSongClick = onSongClick)
	}
}

@Composable
fun AlbumHeader(album: Album?) {
	Crossfade(
		targetState = album,
		animationSpec = tween(LocalDurations.current.crossFade)
	) {
		AlbumsDetailsItem(
			albumArt = it?.albumArt,
			albumName = it?.name,
			artistName = it?.artist
		)
	}
}

@Composable
fun AlbumSongsList(songsList: List<Song>, onSongClick: (Song) -> Unit) {
	Surface(
		modifier = Modifier.padding(horizontal = 16.dp),
		color = MaterialTheme.colors.background,
		shape = MaterialTheme.shapes.medium
	) {
		SongsList(songsList = songsList, onSongClick = onSongClick)
	}
	Spacer(modifier = Modifier.height(50.dp))
}