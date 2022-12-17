package com.looker.core.common.states

sealed class SheetsState {
	object VISIBLE : SheetsState()
	object HIDDEN : SheetsState()
}