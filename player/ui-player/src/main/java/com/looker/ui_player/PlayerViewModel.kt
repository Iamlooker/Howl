package com.looker.ui_player

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import com.looker.components.ComponentConstants.artworkUri
import com.looker.data_music.data.Song

class PlayerViewModel : ViewModel() {

    val playing = mutableStateOf(false)

    var playIcon by mutableStateOf(Icons.Rounded.PlayArrow)

    val progressValue = mutableStateOf(0f)

    val shuffle = mutableStateOf(false)

    val currentSong = mutableStateOf(
        Song(songUri = "".toUri(), albumId = R.drawable.error_image.toLong(), duration = 0)
    )

    val songName: String
        get() = currentSong.value.songName ?: "No Name"
    val artistName: String
        get() = currentSong.value.artistName ?: "No Name"
    val albumArt: Any
        get() = currentSong.value.albumId.artworkUri ?: R.drawable.error_image

    fun onShuffle() {
        shuffle.value = !shuffle.value
    }

    fun onPlayPause() {
        playing.value = !playing.value
        playIcon = if (playing.value) Icons.Rounded.Pause else Icons.Rounded.PlayArrow
    }
}