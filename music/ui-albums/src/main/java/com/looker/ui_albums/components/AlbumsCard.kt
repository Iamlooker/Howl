package com.looker.ui_albums.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import com.looker.components.LargeCard
import com.looker.components.rememberDominantColorState
import com.looker.components.tweenAnimation
import com.looker.core_model.Album
import kotlinx.coroutines.launch

@Composable
fun AlbumsCard(
	modifier: Modifier = Modifier,
	album: Album,
	cardWidth: Dp,
	onClick: () -> Unit
) {
	val backgroundColor = rememberDominantColorState()
	LaunchedEffect(album) { launch { backgroundColor.updateColorsFromImageUrl(album.albumArt) } }

	val animateColor by animateColorAsState(
		targetValue = backgroundColor.color.copy(0.4f),
		animationSpec = tweenAnimation()
	)

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