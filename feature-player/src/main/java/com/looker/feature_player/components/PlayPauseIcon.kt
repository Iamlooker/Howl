package com.looker.feature_player.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.with
import androidx.compose.material.Icon
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.LocalContentColor
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun PlayPauseIcon(
	tint: @Composable () -> Color = { LocalContentColor.current.copy(alpha = LocalContentAlpha.current) },
	icon: () -> ImageVector
) {
	AnimatedContent(
		targetState = icon(),
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
		Icon(imageVector = it, tint = tint(), contentDescription = null)
	}
}