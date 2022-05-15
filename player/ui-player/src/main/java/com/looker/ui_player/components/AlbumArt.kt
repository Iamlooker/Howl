package com.looker.ui_player.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.looker.components.HowlImage
import com.looker.components.ToggleButton

@Composable
fun AlbumArtAndUtils(
	modifier: Modifier = Modifier,
	albumArt: String?,
	icon: ImageVector,
	toggled: Boolean,
	contentDescription: String?,
	albumArtCorner: Int,
	onToggle: () -> Unit,
) {
	Box {
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
			icon = icon,
			toggled = toggled,
			shape = MaterialTheme.shapes.medium,
			contentPadding = PaddingValues(vertical = 16.dp),
			onToggle = onToggle,
			contentDescription = contentDescription
		)
	}
}