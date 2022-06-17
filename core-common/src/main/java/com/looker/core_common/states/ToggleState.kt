package com.looker.core_common.states

sealed class ToggleState {
	object Shuffle : ToggleState()
	object PlayControl : ToggleState()
}