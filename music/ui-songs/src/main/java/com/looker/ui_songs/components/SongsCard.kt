package com.looker.ui_songs.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.looker.components.SmallCard
import com.looker.core_model.Song

@Composable
fun SongsCard(
	modifier: Modifier = Modifier,
	song: Song,
	onClick: () -> Unit
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