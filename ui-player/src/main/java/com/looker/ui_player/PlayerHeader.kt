package com.looker.ui_player

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.looker.domain_music.Song
import com.looker.ui_player.components.AlbumArt
import com.looker.ui_player.components.SongText

@Composable
fun PlayerHeader(
	modifier: Modifier = Modifier,
	song: Song,
	isPlaying: Boolean,
	toggleIcon: ImageVector,
	toggled: Boolean,
	toggleAction: () -> Unit
) {
	Column(
		modifier = modifier,
		verticalArrangement = Arrangement.spacedBy(20.dp),
		horizontalAlignment = Alignment.CenterHorizontally
	) {
		AlbumArt(
			modifier = Modifier
				.fillMaxHeight(0.27f)
				.fillMaxWidth()
				.padding(horizontal = 20.dp),
			isPlaying = isPlaying,
			albumArt = song.albumArt,
			toggleIcon = toggleIcon,
			toggled = toggled,
			onToggle = toggleAction,
			contentDescription = "Play"
		)
		SongText(songName = song.name, artistName = song.artistName)
	}
}