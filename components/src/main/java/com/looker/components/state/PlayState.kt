package com.looker.components.state

sealed class PlayState {
    object PLAYING : PlayState()
    object PAUSED : PlayState()
}