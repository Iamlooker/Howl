package com.looker.feature_player.ui.components

import androidx.annotation.FloatRange
import androidx.compose.material.Slider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun SeekBar(
	modifier: Modifier = Modifier,
	@FloatRange(from = 0.0, to = 1.0)
	progress: Float,
	onValueChange: (Float) -> Unit,
	onValueChanged: () -> Unit
) {
	Slider(
		modifier = modifier,
		value = progress,
		onValueChange = onValueChange,
		onValueChangeFinished = onValueChanged
	)
}