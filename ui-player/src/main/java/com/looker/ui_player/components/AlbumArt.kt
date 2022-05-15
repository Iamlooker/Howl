package com.looker.ui_player.components

import androidx.compose.animation.core.animateIntAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.looker.components.HowlImage
import com.looker.components.ToggleButton
import com.looker.components.localComposers.LocalDurations
import com.looker.components.tweenAnimation

@Composable
fun AlbumArt(
	modifier: Modifier = Modifier,
	albumArt: String?,
	isPlaying: Boolean,
	toggleIcon: ImageVector,
	toggled: Boolean,
	contentDescription: String?,
	onToggle: () -> Unit,
) {
	Box {

		val albumArtCorner by animateIntAsState(
			targetValue = if (isPlaying) 50 else 15,
			animationSpec = tweenAnimation(LocalDurations.current.crossFade)
		)

		HowlImage(
			modifier = modifier.graphicsLayer {
				shape = RoundedCornerShape(albumArtCorner)
				clip = true
			},
			data = albumArt
		)

		ToggleButton(
			modifier = Modifier
				.padding(horizontal = 24.dp, vertical = 8.dp)
				.align(Alignment.BottomEnd),
			toggled = toggled,
			shape = MaterialTheme.shapes.medium,
			contentPadding = PaddingValues(vertical = 16.dp),
			onToggle = onToggle
		) {
			Icon(
				imageVector = toggleIcon,
				contentDescription = contentDescription
			)
		}
	}
}