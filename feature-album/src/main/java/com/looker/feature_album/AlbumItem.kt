package com.looker.feature_album

import androidx.compose.animation.animateColorAsState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import com.looker.core_model.Album
import com.looker.core_ui.components.LargeCard
import com.looker.core_ui.components.rememberDominantColorState
import kotlinx.coroutines.launch

@Composable
fun AlbumItem(
	album: Album,
	modifier: Modifier = Modifier,
	cardWidth: Dp,
	onClick: () -> Unit
) {
	val backgroundColor = rememberDominantColorState()
	LaunchedEffect(album) { launch { backgroundColor.updateColorsFromImageUrl(album.albumArt) } }

	val animateColor by animateColorAsState(targetValue = backgroundColor.color.copy(0.4f))

	LargeCard(
		modifier = modifier,
		imageUrl = album.albumArt,
		title = album.name,
		subText = album.artist,
		cardColor = animateColor,
		imageSize = cardWidth,
		onClick = onClick
	)
}