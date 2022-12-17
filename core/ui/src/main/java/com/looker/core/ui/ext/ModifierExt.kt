package com.looker.core.ui.ext

import androidx.annotation.FloatRange
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer

@Stable
fun Modifier.translate(
	interactionSource: MutableInteractionSource = MutableInteractionSource(),
	maxX: Float = 0f,
	maxY: Float = 0f
): Modifier = composed {
	val isPressed by interactionSource.collectIsPressedAsState()
	val offsetX by animateFloatAsState(targetValue = if (isPressed) maxX else 0f)
	val offsetY by animateFloatAsState(targetValue = if (isPressed) maxY else 0f)
	graphicsLayer {
		translationX = offsetX
		translationY = offsetY
	}
}

@Stable
fun Modifier.backgroundGradient(
	@FloatRange(from = 0.0, to = 1.0) startYPercentage: Float = 1f,
	@FloatRange(from = 0.0, to = 1.0) endYPercentage: Float = 0f,
	color: @Composable () -> Color
): Modifier = composed {
	val animateColor by animateColorAsState(
		targetValue = color(),
		animationSpec = spring(stiffness = Spring.StiffnessLow)
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