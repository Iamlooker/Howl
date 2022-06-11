package com.looker.constants.states

data class ToggleButtonState(
	val toggleState: ToggleState,
	val enabled: Boolean
)

sealed class ToggleState {
	object Shuffle : ToggleState()
	object PlayControl : ToggleState()
}