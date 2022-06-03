package com.looker.ui_albums

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.looker.constants.Resource
import com.looker.core_model.Album
import com.looker.ui_albums.components.AlbumsCard
import com.looker.ui_songs.SongsList

@Composable
fun Albums(
	albumsList: Resource<List<Album>>,
	onAlbumClick: (Album) -> Unit,
) {
	Surface(color = MaterialTheme.colors.background) {
		when (albumsList) {
			is Resource.Success -> Albums(modifier = Modifier.fillMaxSize(), albumsList = albumsList.data, onAlbumClick = onAlbumClick)
			is Resource.Loading -> com.looker.ui_songs.LoadingState()
			is Resource.Error -> Unit
		}
	}
}

@Composable
private fun Albums(
	modifier: Modifier = Modifier,
	albumsList: List<Album>?,
	onAlbumClick: (Album) -> Unit,
) {
	AlbumsList(
		modifier = modifier,
		albumsList = albumsList,
		onAlbumClick = onAlbumClick
	)
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
fun AlbumsList(
	modifier: Modifier = Modifier,
	albumsList: List<Album>?,
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
		items(albumsList ?: emptyList()) { album ->
			AlbumsCard(album = album, cardWidth = width) { onAlbumClick(album) }
		}
	}
}