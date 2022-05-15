package com.looker.ui_player

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.looker.ui_player.components.AlbumArtAndUtils
import com.looker.ui_player.components.SongText

@Composable
fun PlayerHeader(
	modifier: Modifier = Modifier,
	albumArt: String?,
	songName: String?,
	artistName: String?,
	onImageIcon: ImageVector,
	toggled: Boolean,
	imageCorner: Int = 50,
	toggleAction: () -> Unit
) {
	Column(
		modifier = modifier,
		verticalArrangement = Arrangement.spacedBy(20.dp),
		horizontalAlignment = Alignment.CenterHorizontally
	) {
		AlbumArtAndUtils(
			modifier = Modifier
				.fillMaxHeight(0.27f)
				.fillMaxWidth()
				.padding(horizontal = 20.dp),
			albumArt = albumArt,
			icon = onImageIcon,
			toggled = toggled,
			onToggle = toggleAction,
			albumArtCorner = imageCorner,
			contentDescription = "Play"
		)
		SongText(songName = songName, artistName = artistName)
	}
}