package com.looker.ui_player

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.looker.core_model.Song
import com.looker.ui_player.components.AlbumArt
import com.looker.ui_player.components.SongText

@Composable
fun PlayerHeader(
	modifier: Modifier = Modifier,
	song: Song,
	isPlaying: Boolean,
	toggled: Boolean,
	toggleAction: () -> Unit,
	toggleIcon: @Composable () -> Unit
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
			onToggle = toggleAction
		)
		SongText {
			Text(
				text = song.name,
				style = MaterialTheme.typography.h4,
				maxLines = 2,
				overflow = TextOverflow.Ellipsis,
				textAlign = TextAlign.Center
			)
			Text(
				text = song.artist,
				style = MaterialTheme.typography.subtitle1,
				fontWeight = FontWeight.SemiBold,
				maxLines = 1,
				overflow = TextOverflow.Ellipsis,
				textAlign = TextAlign.Center
			)
		}
	}
}