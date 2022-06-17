package com.looker.core_common.states

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.ui.graphics.vector.ImageVector

sealed class ToggleState {
	object Shuffle : ToggleState()
	object PlayControl : ToggleState()
}

data class ToggleButtonState(
	val toggleState: ToggleState,
	val enabled: Boolean,
	val icon: ImageVector = Icons.Rounded.PlayArrow
)