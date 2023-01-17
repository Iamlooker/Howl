package com.looker.onboarding.components

import androidx.compose.animation.animateColorAsState
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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import com.looker.onboarding.utils.PermissionButtonState

@Composable
fun PermissionButton(
	buttonState: PermissionButtonState,
	onClick: () -> Unit
) {
	val color by animateColorAsState(buttonState.color.copy(0.4f))
	OutlinedButton(
		onClick = onClick,
		colors = buttonColors(color),
		shape = CircleShape,
		contentPadding = PaddingValues(vertical = 20.dp, horizontal = 30.dp)
	) {
		Row(
			modifier = Modifier.wrapContentSize(),
			horizontalArrangement = Arrangement.spacedBy(5.dp),
			verticalAlignment = Alignment.CenterVertically
		) {
			buttonState.icon()
			Text(
				text = AnnotatedString(
					text = buttonState.text
				),
				modifier = Modifier.animateContentSize()
			)
		}
	}
}