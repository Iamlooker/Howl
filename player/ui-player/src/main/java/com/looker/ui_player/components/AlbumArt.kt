package com.looker.ui_player.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.looker.components.HowlImage
import com.looker.components.ToggleButton
import com.looker.components.ext.rippleClick

@Composable
fun AlbumArtAndUtils(
	modifier: Modifier = Modifier,
	albumArt: String?,
	icon: ImageVector,
	toggled: Boolean,
	contentDescription: String?,
	albumArtCorner: Int,
	onToggle: () -> Unit,
	overlayItems: @Composable RowScope.() -> Unit,
) {
	var overlayVisible by remember { mutableStateOf(false) }

	Box {
		AlbumArt(
			modifier = modifier.graphicsLayer {
				shape = RoundedCornerShape(albumArtCorner)
				clip = true
			},
			albumArt = albumArt,
			overlayVisible = overlayVisible,
			onClick = { overlayVisible = !overlayVisible },
			overlayItems = overlayItems
		)

		ToggleButton(
			modifier = Modifier
				.padding(horizontal = 24.dp, vertical = 8.dp)
				.align(Alignment.BottomEnd),
			icon = icon,
			toggled = toggled,
			shape = MaterialTheme.shapes.medium,
			contentPadding = PaddingValues(vertical = 16.dp),
			onToggle = onToggle,
			contentDescription = contentDescription
		)
	}
}

@Composable
fun AlbumArt(
	modifier: Modifier = Modifier,
	albumArt: String?,
	overlayVisible: Boolean,
	onClick: () -> Unit,
	overlayItems: @Composable (RowScope.() -> Unit),
) {
	Box(modifier) {
		HowlImage(
			modifier = Modifier
				.matchParentSize()
				.rippleClick(onClick = onClick),
			data = albumArt
		)
		AnimatedVisibility(
			modifier = Modifier.matchParentSize(),
			enter = fadeIn(),
			exit = fadeOut(),
			visible = overlayVisible
		) {
			AlbumArtOverlay(
				onClick = onClick,
				content = overlayItems
			)
		}
	}
}

@Composable
fun AlbumArtOverlay(
	modifier: Modifier = Modifier,
	onClick: () -> Unit,
	content: @Composable RowScope.() -> Unit,
) {
	Surface(
		modifier = modifier.rippleClick(onClick = onClick),
		color = MaterialTheme.colors.onBackground.copy(0.4f),
		content = {
			Row(
				verticalAlignment = Alignment.CenterVertically,
				horizontalArrangement = Arrangement.SpaceEvenly,
				content = content
			)
		}
	)
}