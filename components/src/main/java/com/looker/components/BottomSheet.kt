package com.looker.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.looker.components.localComposers.LocalDurations
import com.looker.components.localComposers.LocalElevations

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BottomSheets(
	modifier: Modifier = Modifier,
	state: ModalBottomSheetState,
	sheetContent: @Composable ColumnScope.() -> Unit,
	content: @Composable () -> Unit
) {
	ModalBottomSheetLayout(
		modifier = modifier,
		sheetElevation = LocalElevations.current.default,
		sheetState = state,
		sheetContent = sheetContent,
		content = content,
		sheetBackgroundColor = MaterialTheme.colors.background,
		scrimColor = MaterialTheme.colors.background.copy(0.3f),
		sheetShape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
	)
}

/**
 * [angle] = 0f is Arrow Up
 *
 * [angle] = 1f is Arrow Down
 */
@Composable
fun HandleIcon(
	angle: Float,
	backgroundColor: Color = MaterialTheme.colors.background,
	onClick: () -> Unit = {}
) {
	Box(
		modifier = Modifier
			.fillMaxWidth()
			.background(backgroundColor)
			.clickable { onClick() },
		contentAlignment = Alignment.Center
	) {
		CanvasHandleIcon(
			modifier = Modifier
				.size(30.dp)
				.padding(6.dp),
			angle = angle
		)
	}
}

@Composable
fun CanvasHandleIcon(
	modifier: Modifier = Modifier,
	angle: Float,
	color: Color = MaterialTheme.colors.onBackground
) {
	val animateIcon by animateFloatAsState(
		targetValue = angle * 2,
		animationSpec = tweenAnimation(LocalDurations.current.crossFade)
	)

	Canvas(modifier = modifier) {
		drawLine(
			color = color,
			strokeWidth = 5f,
			start = Offset(0f, center.y),
			end = Offset(center.x, animateIcon * center.y)
		)
		drawLine(
			color = color,
			strokeWidth = 5f,
			start = Offset(center.x, animateIcon * center.y),
			end = Offset(size.width, center.y)
		)
	}
}