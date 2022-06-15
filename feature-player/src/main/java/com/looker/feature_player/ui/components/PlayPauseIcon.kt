package com.looker.feature_player.ui.components

import androidx.compose.animation.*
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun PlayPauseIcon(icon: ImageVector) {
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
		Icon(imageVector = it, contentDescription = null)
	}
}