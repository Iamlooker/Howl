package com.looker.feature_player.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.SkipNext
import androidx.compose.material.icons.rounded.SkipPrevious
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.looker.components.ShapedIconButton
import com.looker.components.overBackground

@Composable
fun PlayAndSkipButton(
	modifier: Modifier = Modifier,
	skipNextClick: () -> Unit,
	playButton: @Composable RowScope.() -> Unit
) {
	Row(
		modifier = modifier.fillMaxWidth(),
		horizontalArrangement = Arrangement.spacedBy(20.dp)
	) {
		playButton()
		ShapedIconButton(
			modifier = Modifier
				.height(60.dp)
				.weight(1f),
			onClick = skipNextClick,
			backgroundColor = MaterialTheme.colors.secondaryVariant.overBackground(0.9f),
			contentColor = MaterialTheme.colors.onSecondary,
			icon = Icons.Rounded.SkipNext
		)
	}
}

@Composable
fun PreviousAndSeekBar(
	modifier: Modifier = Modifier,
	skipPrevClick: () -> Unit,
	progressBar: @Composable () -> Unit
) {
	Row(
		modifier = modifier.fillMaxWidth(),
		horizontalArrangement = Arrangement.spacedBy(20.dp)
	) {
		ShapedIconButton(
			modifier = Modifier
				.height(60.dp)
				.weight(1f),
			onClick = skipPrevClick,
			backgroundColor = MaterialTheme.colors.secondaryVariant.overBackground(0.9f),
			contentColor = MaterialTheme.colors.onSecondary,
			icon = Icons.Rounded.SkipPrevious
		)
		Box(modifier = Modifier.weight(3f)) {
			progressBar()
		}
	}
}