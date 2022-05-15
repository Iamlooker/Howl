package com.looker.onboarding.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ButtonDefaults.buttonColors
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp

@Composable
fun AnimatedButton(
	buttonText: String,
	buttonIcon: @Composable () -> Unit,
	buttonColor: Color,
	onClick: () -> Unit,
) {

	OutlinedButton(
		onClick = onClick,
		colors = buttonColors(backgroundColor = buttonColor.copy(0.4f)),
		shape = CircleShape,
		contentPadding = PaddingValues(vertical = 20.dp, horizontal = 30.dp)
	) {
		Row(
			modifier = Modifier.wrapContentSize(),
			horizontalArrangement = Arrangement.spacedBy(5.dp),
			verticalAlignment = Alignment.CenterVertically
		) {
			buttonIcon()
			Text(
				text = AnnotatedString(
					text = buttonText
				),
				modifier = Modifier.animateContentSize()
			)
		}
	}
}