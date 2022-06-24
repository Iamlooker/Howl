package com.looker.feature_player.components

import androidx.annotation.FloatRange
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.looker.core_ui.WaveySeekbar

@Composable
fun SeekBar(
	modifier: Modifier = Modifier,
	@FloatRange(from = 0.0, to = 1.0)
	progress: Float,
	onValueChange: (Float) -> Unit,
	onValueChanged: () -> Unit
) {
	WaveySeekbar(
		modifier = modifier,
		value = progress,
		onValueChange = onValueChange,
		onValueChangeFinished = onValueChanged
	)
}