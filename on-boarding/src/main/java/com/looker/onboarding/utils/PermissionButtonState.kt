package com.looker.onboarding.utils

import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Done
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color

@Immutable
sealed class PermissionButtonState(
	val text: String,
	val icon: @Composable () -> Unit,
	val color: Color
) {
	@Stable
	object GRANTED : PermissionButtonState(
		text = "Granted",
		icon = { Icon(imageVector = Icons.Rounded.Done, contentDescription = null) },
		color = green
	)

	@Stable
	object DENIED : PermissionButtonState(
		text = "Denied",
		icon = { Icon(imageVector = Icons.Rounded.Close, contentDescription = null) },
		color = red
	)

	@Stable
	object WaitingAction : PermissionButtonState(
		text = "Grant Permission",
		icon = { },
		color = orange
	)
}

private val red = Color(0xFFFF5F5F)
private val orange = Color(0xFFFF9e80)
private val green = Color(0xFF69f0ae)
