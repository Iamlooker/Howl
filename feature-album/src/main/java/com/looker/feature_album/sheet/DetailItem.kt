package com.looker.feature_album.sheet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.looker.components.HowlImage
import com.looker.components.TitleSubText
import com.looker.core_model.Album

@Composable
fun AlbumsDetailsItem(
	album: Album,
	modifier: Modifier = Modifier,
	albumArt: @Composable BoxScope.() -> Unit
) {
	Column(
		modifier = modifier
			.clip(MaterialTheme.shapes.large)
			.background(MaterialTheme.colors.background),
		verticalArrangement = Arrangement.Top,
		horizontalAlignment = Alignment.CenterHorizontally
	) {
		DetailsArt(albumArt = albumArt)
		DetailsText(albumName = album.name, artistName = album.artist)
		Spacer(modifier = Modifier.height(20.dp))
	}
}

@Composable
fun DetailsArt(
	modifier: Modifier = Modifier,
	albumArt: @Composable BoxScope.() -> Unit
) {
	Box(
		modifier = modifier
			.fillMaxWidth()
			.height(320.dp)
			.padding(20.dp)
	) { albumArt() }
}

@Composable
fun DetailsText(
	albumName: String,
	artistName: String
) {
	TitleSubText(
		title = albumName,
		subText = artistName,
		titleTextStyle = MaterialTheme.typography.h5,
		subTextTextStyle = MaterialTheme.typography.body1,
		itemTextAlignment = Alignment.CenterHorizontally,
		textAlign = TextAlign.Center,
		maxLines = 1
	)
}

@Preview
@Composable
fun AlbumDetailPreview() {
	AlbumsDetailsItem(
		Album(
			name = "Title",
			artist = "Artist Name"
		)
	) {
		HowlImage(
			modifier = Modifier.matchParentSize(),
			data = "",
			shape = MaterialTheme.shapes.large
		)
	}
}