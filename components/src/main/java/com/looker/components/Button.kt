package com.looker.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.ButtonDefaults.buttonColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun ShapedIconButton(
	modifier: Modifier = Modifier,
	backgroundColor: Color = MaterialTheme.colors.primary,
	contentColor: Color = contentColorFor(backgroundColor),
	buttonElevation: ButtonElevation = ButtonDefaults.elevation(0.dp, 0.dp, 0.dp, 0.dp),
	contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
	onClick: () -> Unit,
	icon: @Composable RowScope.() -> Unit
) {
	Button(
		modifier = modifier,
		onClick = onClick,
		colors = buttonColors(
			backgroundColor = backgroundColor,
			contentColor = contentColor
		),
		elevation = buttonElevation,
		contentPadding = contentPadding,
		content = icon
	)
}

/**
 * Don't use with changing icons
 */
@Composable
fun ShapedIconButton(
	modifier: Modifier = Modifier,
	backgroundColor: Color = MaterialTheme.colors.primary,
	contentColor: Color = contentColorFor(backgroundColor),
	buttonElevation: ButtonElevation = ButtonDefaults.elevation(0.dp, 0.dp, 0.dp, 0.dp),
	contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
	shape: Shape = CircleShape,
	onClick: () -> Unit,
	icon: ImageVector
) {
	Button(
		modifier = modifier,
		onClick = onClick,
		colors = buttonColors(
			backgroundColor = backgroundColor,
			contentColor = contentColor
		),
		elevation = buttonElevation,
		shape = shape,
		contentPadding = contentPadding
	) {
		Icon(imageVector = icon, contentDescription = null)
	}
}