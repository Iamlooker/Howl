package com.looker.howlmusic

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Shuffle
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModel

class HowlViewModel : ViewModel() {

    val playing = mutableStateOf(false)

    fun shufflePlay(pos: Float): ImageVector =
        if (pos == 1f) Icons.Rounded.Shuffle
        else Icons.Default.PlayArrow


    fun onPlayPause() {
        playing.value = !playing.value
        playIcon = if (playing.value) Icons.Rounded.Pause else Icons.Rounded.PlayArrow
    }

    var playIcon by mutableStateOf(Icons.Rounded.PlayArrow)
}