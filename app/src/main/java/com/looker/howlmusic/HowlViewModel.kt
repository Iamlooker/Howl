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
import com.looker.data_music.data.Song

class HowlViewModel : ViewModel() {

    private val _playing = MutableLiveData<Boolean>()
    private val _shuffle = MutableLiveData<Boolean>()
    private val _progress = MutableLiveData<Float>()
    private val _playIcon = MutableLiveData<ImageVector>()
    private val _handleIcon = MutableLiveData<ImageVector>()
    private val _currentSong = MutableLiveData<Song>()

    val playing: LiveData<Boolean> = _playing
    val shuffle: LiveData<Boolean> = _shuffle
    val progress: LiveData<Float> = _progress
    val handleIcon: LiveData<ImageVector> = _handleIcon
    val currentSong: LiveData<Song> = _currentSong
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

    fun setHandleIcon(pos: Float) {
        _handleIcon.value = if (pos == 1f) Icons.Rounded.ArrowDropUp
        else Icons.Rounded.ArrowDropDown
    }

    fun onSongClicked(song: Song) {
        _currentSong.value = song
    }

    fun onSeek(seekTo: Float) {
        _progress.value = seekTo
    }
}