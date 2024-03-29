package com.looker.core.album.sheet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.looker.core.ui.components.TitleSubText

@Composable
fun AlbumsDetailsItem(
	modifier: Modifier = Modifier,
	albumText: @Composable () -> Unit,
	albumArt: @Composable BoxScope.() -> Unit
) {
	Column(
		modifier = modifier.background(MaterialTheme.colors.background, MaterialTheme.shapes.large),
		verticalArrangement = Arrangement.Top,
		horizontalAlignment = Alignment.CenterHorizontally
	) {
		DetailsArt(albumArt = albumArt)
		albumText()
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
			.padding(20.dp),
		content = albumArt
	)
}

@Composable
fun DetailsText(
	albumName: String,
	artistName: String
) {
	TitleSubText(
		title = albumName,
		subText = artistName,
		titleTextStyle = MaterialTheme.typography.h3,
		subTextTextStyle = MaterialTheme.typography.h5,
		itemTextAlignment = Alignment.CenterHorizontally,
		textAlign = TextAlign.Center,
		maxLines = 1
	)
}