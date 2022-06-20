package com.looker.core_common.states

sealed class SheetsState {
	object VISIBLE : SheetsState()
	object HIDDEN : SheetsState()
}