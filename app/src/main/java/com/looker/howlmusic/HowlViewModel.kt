package com.looker.howlmusic

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Shuffle
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModel

class HowlViewModel : ViewModel() {

    val playing = mutableStateOf(false)
    val shuffle = mutableStateOf(false)
    val progress = mutableStateOf(0f)

    fun shufflePlay(pos: Float): ImageVector =
        if (pos == 1f) Icons.Rounded.Shuffle
        else playIcon()

    fun onPlayPause() {
        playing.value = !playing.value
    }

    fun playIcon(): ImageVector =
        if (playing.value) Icons.Rounded.Pause else Icons.Rounded.PlayArrow

    fun toggle(pos: Float): Boolean =
        if (pos == 1f) shuffle.value
        else playing.value

    fun onToggle(pos: Float) {
        if (pos == 1f) shuffle.value = !shuffle.value
        else playing.value = !playing.value
    }

    fun onSeek(seekTo: Float) {
        progress.value = seekTo
    }
}