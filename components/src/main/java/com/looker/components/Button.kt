package com.looker.components

import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.material.*
import androidx.compose.material.ButtonDefaults.buttonColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.looker.components.localComposers.LocalDurations

@Composable
fun ShapedIconButton(
	modifier: Modifier = Modifier,
	icon: ImageVector,
	backgroundColor: Color = MaterialTheme.colors.primary,
	contentColor: Color = contentColorFor(backgroundColor),
	buttonElevation: ButtonElevation = ButtonDefaults.elevation(0.dp, 0.dp, 0.dp, 0.dp),
	contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
	contentDescription: String?,
	onClick: () -> Unit,
) {
	Button(
		modifier = modifier,
		onClick = onClick,
		colors = buttonColors(
			backgroundColor = backgroundColor,
			contentColor = contentColor
		),
		elevation = buttonElevation,
		contentPadding = contentPadding
	) {
		Crossfade(
			targetState = icon,
			animationSpec = tweenAnimation(LocalDurations.current.crossFade)
		) {
			Icon(
				imageVector = it,
				contentDescription = contentDescription
			)
		}
	}
}

@Composable
fun ToggleButton(
	modifier: Modifier = Modifier,
	toggled: Boolean,
	icon: ImageVector,
	shape: CornerBasedShape = CircleShape,
	activeColor: Color = MaterialTheme.colors.secondaryVariant,
	contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
	onToggle: () -> Unit,
	contentDescription: String?
) {
	val toggleColor by animateColorAsState(
		targetValue = if (toggled) activeColor.compositeOverBackground(0.6f)
		else MaterialTheme.colors.surface,
		animationSpec = tweenAnimation(LocalDurations.current.crossFade)
	)

	ShapedIconButton(
		modifier = modifier.clip(shape),
		icon = icon,
		backgroundColor = toggleColor,
		contentPadding = contentPadding,
		onClick = onToggle,
		contentDescription = contentDescription
	)
}