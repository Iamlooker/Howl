package com.looker.feature_player

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.looker.feature_player.components.AlbumArt
import com.looker.feature_player.components.PlayAndSkipButton
import com.looker.feature_player.components.PreviousAndSeekBar
import com.looker.feature_player.components.SongText

@Composable
fun PlayerHeader(
	modifier: Modifier = Modifier,
	songText: @Composable ColumnScope.() -> Unit,
	toggleIcon: @Composable BoxScope.() -> Unit,
	albumArt: @Composable BoxScope.() -> Unit
) {
	Column(
		modifier = modifier.fillMaxWidth(),
		horizontalAlignment = Alignment.CenterHorizontally,
		verticalArrangement = Arrangement.spacedBy(20.dp)
	) {
		AlbumArt(
			modifier = Modifier
				.fillMaxWidth()
				.fillMaxHeight(0.27f),
			button = toggleIcon,
			image = albumArt
		)
		SongText(text = songText)
	}
}

@Composable
fun Controls(
	modifier: Modifier = Modifier,
	skipNextClick: () -> Unit,
	skipPrevClick: () -> Unit,
	playButton: @Composable RowScope.() -> Unit,
	progressBar: @Composable () -> Unit
) {
	Column(
		modifier = modifier.padding(20.dp),
		verticalArrangement = Arrangement.spacedBy(20.dp)
	) {
		PlayAndSkipButton(
			skipNextClick = skipNextClick,
			playButton = playButton
		)
		PreviousAndSeekBar(
			skipPrevClick = skipPrevClick,
			progressBar = progressBar
		)
	}
}