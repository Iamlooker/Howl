package com.looker.ui_albums

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.looker.components.ext.backgroundGradient
import com.looker.components.localComposers.LocalDurations
import com.looker.components.tweenAnimation
import com.looker.domain_music.Album
import com.looker.domain_music.Song
import com.looker.ui_albums.components.AlbumsDetailsItem
import com.looker.ui_songs.SongsList

@Composable
fun AlbumsBottomSheetContent(
	modifier: Modifier = Modifier,
	currentAlbum: Album?,
	songsList: List<Song>,
	dominantColor: Color = MaterialTheme.colors.surface
) {
	AlbumBottomSheetItem(
		modifier = modifier,
		album = currentAlbum,
		albumDominantColor = dominantColor,
		songsList = songsList
	)
}

@Composable
fun AlbumBottomSheetItem(
	modifier: Modifier = Modifier,
	album: Album?,
	albumDominantColor: Color,
	songsList: List<Song>
) {
	Column(modifier = modifier.backgroundGradient(albumDominantColor)) {
		AlbumHeader(album = album)
		AlbumSongsList(songsList = songsList)
	}
}

@Composable
fun AlbumHeader(album: Album?) {
	Crossfade(
		targetState = album,
		animationSpec = tweenAnimation(LocalDurations.current.crossFade)
	) {
		AlbumsDetailsItem(
			albumArt = it?.albumArt,
			albumName = it?.albumName,
			artistName = it?.artistName
		)
	}
}

@Composable
fun AlbumSongsList(songsList: List<Song>) {
	SongsList(songsList = songsList)
	Spacer(modifier = Modifier.height(50.dp))
}