package com.looker.onboarding.utils

import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Done
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

sealed class ButtonState(
	val text: String,
	val icon: @Composable () -> Unit,
	val color: Color
) {
	object GRANTED : ButtonState(
		text = "Granted",
		icon = { Icon(imageVector = Icons.Rounded.Done, contentDescription = null) },
		color = green
	)

	object DENIED : ButtonState(
		text = "Denied",
		icon = { Icon(imageVector = Icons.Rounded.Close, contentDescription = null) },
		color = orange
	)

	object WaitingAction : ButtonState(
		text = "Grant Permission",
		icon = { },
		color = orange
	)
}

private val orange = Color(0xFFFF9e80)
private val green = Color(0xFF69f0ae)
