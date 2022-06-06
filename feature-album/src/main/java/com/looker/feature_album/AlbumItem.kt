package com.looker.feature_album

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import com.looker.components.LargeCard
import com.looker.components.localComposers.LocalDurations
import com.looker.components.rememberDominantColorState
import com.looker.core_model.Album
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

	val animateColor by animateColorAsState(
		targetValue = backgroundColor.color.copy(0.4f),
		animationSpec = tween(LocalDurations.current.fadeIn)
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