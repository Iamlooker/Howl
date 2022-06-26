package com.looker.core_ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.looker.core_model.Song

@Composable
fun SongItem(
	onClick: () -> Unit,
	modifier: Modifier = Modifier,
	song: Song
) {
	SmallCard(
		modifier = modifier.fillMaxWidth().padding(horizontal = 8.dp),
		imageUrl = song.albumArt,
		title = song.name,
		subText = song.artist,
		imageSize = 56.dp,
		onClick = onClick
	)
}