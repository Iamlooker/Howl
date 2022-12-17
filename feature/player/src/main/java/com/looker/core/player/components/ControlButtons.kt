package com.looker.core.player.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.SkipNext
import androidx.compose.material.icons.rounded.SkipPrevious
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.looker.core.ui.components.OpaqueIconButton
import com.looker.core.ui.ext.translate
import com.looker.core.ui.components.overBackground

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
		val interactionSource = remember { MutableInteractionSource() }
		playButton()
		OpaqueIconButton(
			modifier = Modifier
				.height(60.dp)
				.weight(1f)
				.translate(interactionSource, maxX = 10f),
			onClick = skipNextClick,
			interactionSource = interactionSource,
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
		val interactionSource = remember { MutableInteractionSource() }
		OpaqueIconButton(
			modifier = Modifier
				.height(60.dp)
				.weight(1f)
				.translate(interactionSource, maxX = -10f),
			onClick = skipPrevClick,
			interactionSource = interactionSource,
			backgroundColor = MaterialTheme.colors.secondaryVariant.overBackground(0.9f),
			contentColor = MaterialTheme.colors.onSecondary,
			icon = Icons.Rounded.SkipPrevious
		)
		Box(modifier = Modifier.weight(3f)) {
			progressBar()
		}
	}
}