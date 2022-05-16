package com.looker.feature_player

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.looker.feature_player.components.AlbumArt
import com.looker.feature_player.components.SongText

@Composable
fun PlayerHeader(
	modifier: Modifier = Modifier,
	onToggleClick: () -> Unit,
	songText: @Composable ColumnScope.() -> Unit,
	toggleIcon: @Composable RowScope.() -> Unit,
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
			onButtonClick = onToggleClick,
			buttonIcon = toggleIcon,
			image = albumArt
		)
		SongText(text = songText)
	}
}