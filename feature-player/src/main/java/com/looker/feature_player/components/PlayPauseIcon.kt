package com.looker.feature_player.components

import androidx.compose.animation.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun PlayPauseIcon(
	icon: ImageVector,
	tint: Color = MaterialTheme.colors.onBackground
) {
	AnimatedContent(
		targetState = icon,
		transitionSpec = {
			when (targetState) {
				Icons.Rounded.PlayArrow -> {
					slideInVertically { height -> -height } + fadeIn() with
							slideOutVertically { height -> height } + fadeOut()
				}
				else -> {
					slideInVertically { height -> height } + fadeIn() with
							slideOutVertically { height -> -height } + fadeOut()
				}
			}.using(SizeTransform(false))
		}

	) {
		Icon(imageVector = it, tint = tint, contentDescription = null)
	}
}