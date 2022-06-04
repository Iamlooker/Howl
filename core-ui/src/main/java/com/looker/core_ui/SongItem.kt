package com.looker.core_ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.looker.components.SmallCard
import com.looker.core_model.Song

@Composable
fun SongItem(
	onClick: () -> Unit,
	modifier: Modifier = Modifier,
	song: Song
) {
	SmallCard(
		modifier = modifier,
		imageUrl = song.albumArt,
		title = song.name,
		subText = song.artist,
		imageSize = 62.dp,
		onClick = onClick
	)
}