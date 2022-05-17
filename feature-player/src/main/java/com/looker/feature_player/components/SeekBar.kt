package com.looker.feature_player.components

import androidx.compose.material.Slider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun SeekBar(
	modifier: Modifier = Modifier,
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