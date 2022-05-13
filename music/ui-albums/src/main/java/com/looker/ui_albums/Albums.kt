package com.looker.ui_albums

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.looker.domain_music.Album
import com.looker.ui_albums.components.AlbumsCard

@Composable
fun Albums(
	albumsList: List<Album>,
	onAlbumClick: (Album) -> Unit,
) {
	Albums(
		modifier = Modifier.fillMaxSize(),
		albumsList = albumsList,
		onAlbumClick = onAlbumClick
	)
}

@Composable
private fun Albums(
	modifier: Modifier = Modifier,
	albumsList: List<Album>,
	onAlbumClick: (Album) -> Unit,
) {
	AlbumsList(
		modifier = modifier,
		albumsList = albumsList,
		onAlbumClick = onAlbumClick
	)
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AlbumsList(
	modifier: Modifier = Modifier,
	albumsList: List<Album>,
	onAlbumClick: (Album) -> Unit,
) {
	val width = with(LocalConfiguration.current) {
		val ratio = screenWidthDp / 150
		screenWidthDp.dp / ratio - 16.dp
	}

	LazyVerticalGrid(
		modifier = modifier,
		columns = GridCells.Adaptive(150.dp)
	) {
		items(albumsList) { album ->
			AlbumsCard(album = album, cardWidth = width) { onAlbumClick(album) }
		}
	}
}