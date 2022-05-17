package com.looker.feature_player.components

import androidx.compose.animation.Crossfade
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun PlayPauseIcon(
	icon: ImageVector,
	modifier: Modifier = Modifier
) {
	Crossfade(modifier = modifier, targetState = icon) {
		Icon(imageVector = it, contentDescription = null)
	}
}