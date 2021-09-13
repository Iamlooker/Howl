package com.looker.howlmusic

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.material.icons.rounded.ArrowDropUp
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HowlViewModel : ViewModel() {


    private val _playing = MutableLiveData(false)
    private val _shuffle = MutableLiveData(false)
    private val _progress = MutableLiveData(0f)
    private val _playIcon = MutableLiveData(Icons.Rounded.PlayArrow)

    val playing: LiveData<Boolean> = _playing
    val shuffle: LiveData<Boolean> = _shuffle
    val progress: LiveData<Float> = _progress
    val playIcon: LiveData<ImageVector>
        get() {
            _playIcon.value = if (_playing.value == true) Icons.Rounded.Pause
            else Icons.Rounded.PlayArrow
            return _playIcon
        }

    fun onPlayPause() {
        _playing.value = _playing.value?.not()
    }

    fun onToggle(pos: Float) {
        if (pos > 0f) _shuffle.value = _shuffle.value?.not()
        else onPlayPause()
    }

    fun handleIcon(pos: Float): ImageVector =
        if (pos > 0f) Icons.Rounded.ArrowDropUp
        else Icons.Rounded.ArrowDropDown


    fun onSeek(seekTo: Float) {
        _progress.value = seekTo
    }
}