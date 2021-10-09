package com.looker.components.state


sealed class SheetsState {
    object VISIBLE : SheetsState()
    object HIDDEN : SheetsState()
}