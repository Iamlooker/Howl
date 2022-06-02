package com.looker.components.ext

import androidx.annotation.FloatRange
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.looker.components.localComposers.LocalDurations

fun Modifier.backgroundGradient(
	color: Color,
	@FloatRange(from = 0.0, to = 1.0) startYPercentage: Float = 1f,
	@FloatRange(from = 0.0, to = 1.0) endYPercentage: Float = 0f,
): Modifier = composed {
	val animateColor by animateColorAsState(
		targetValue = color,
		animationSpec = tween(LocalDurations.current.crossFade)
	)
	val colors = remember(animateColor) {
		listOf(animateColor.copy(alpha = 0f), animateColor)
	}

	var height by remember { mutableStateOf(0f) }
	val brush = remember(animateColor, startYPercentage, endYPercentage, height) {
		Brush.verticalGradient(
			colors = colors,
			startY = height * startYPercentage,
			endY = height * endYPercentage
		)
	}

	drawBehind {
		height = size.height
		drawRect(brush = brush)
	}
}