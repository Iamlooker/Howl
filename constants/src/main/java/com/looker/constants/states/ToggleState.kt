package com.looker.constants.states

sealed class ToggleState {
	object Shuffle : ToggleState()
	object PlayControl : ToggleState()
}