package com.looker.constants.states

sealed class ToggleState(var enabled: Boolean = false) {
	object Shuffle : ToggleState()
	object PlayControl : ToggleState()
}