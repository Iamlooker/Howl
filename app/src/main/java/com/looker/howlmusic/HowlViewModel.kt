package com.looker.howlmusic

import androidx.compose.material.BackdropValue
import androidx.compose.material.BackdropValue.Concealed
import androidx.compose.material.BackdropValue.Revealed
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Shuffle
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.looker.components.SheetsState
import com.looker.domain_music.Song
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HowlViewModel
@Inject constructor(private val exoPlayer: SimpleExoPlayer) : ViewModel() {

    private var playIcon by mutableStateOf(Icons.Rounded.PlayArrow)

    private val _playing = MutableLiveData<Boolean>()
    private val _progress = MutableLiveData<Float>()
    private val _toggleIcon = MutableLiveData<ImageVector>()
    private val _handleIcon = MutableLiveData<Float>()
    private val _currentSong = MutableLiveData<Song>()
    private val _enableGesture = MutableLiveData<Boolean>()
    private val _backdropValue = MutableLiveData<SheetsState>()

    val playing: LiveData<Boolean> = _playing
    val progress: LiveData<Float> = _progress
    val toggleIcon: LiveData<ImageVector> = _toggleIcon
    val handleIcon: LiveData<Float> = _handleIcon
    val currentSong: LiveData<Song> = _currentSong
    val enableGesture: LiveData<Boolean> = _enableGesture
    val backdropValue: LiveData<SheetsState> = _backdropValue

    @ExperimentalMaterialApi
    fun setBackdropValue(currentValue: BackdropValue) {
        _backdropValue.value = when (currentValue) {
            Concealed -> SheetsState.HIDDEN
            Revealed -> SheetsState.VISIBLE
        }
    }

    fun gestureState(allowGesture: Boolean) {
        _enableGesture.value = allowGesture
    }

    private fun updatePlayIcon(isPlaying: Boolean) {
        playIcon = if (isPlaying) Icons.Rounded.Pause else Icons.Rounded.PlayArrow
    }

    fun onPlayPause() {
        if (_playing.value == true) exoPlayer.pause() else exoPlayer.play()
        _playing.value = exoPlayer.isPlaying
        updatePlayIcon(exoPlayer.isPlaying)
    }

    fun onToggle(currentState: SheetsState) {
        when (currentState) {
            SheetsState.HIDDEN -> onPlayPause()
            SheetsState.ToHIDDEN -> onPlayPause()
            else -> {
            }
        }
    }

    fun setToggleIcon(currentState: SheetsState) {
        _toggleIcon.value = when (currentState) {
            SheetsState.ToHIDDEN -> playIcon
            SheetsState.ToVISIBLE -> Icons.Rounded.Shuffle
            SheetsState.HIDDEN -> playIcon
            SheetsState.VISIBLE -> Icons.Rounded.Shuffle
        }
    }

    fun setHandleIcon(currentState: SheetsState) {
        _handleIcon.value = when (currentState) {
            SheetsState.ToHIDDEN -> 1f
            SheetsState.ToVISIBLE -> 1f
            SheetsState.HIDDEN -> 0f
            SheetsState.VISIBLE -> 2f
        }
    }

    fun onSongClicked(song: Song) {
        _playing.value = true
        updatePlayIcon(true)
        _currentSong.value = song
        exoPlayer.apply {
            clearMediaItems()
            setMediaItem(MediaItem.fromUri(song.songUri))
            prepare()
            play()
        }
    }

    fun onSeek(seekTo: Float) {
        _progress.value = seekTo
        exoPlayer.seekTo((exoPlayer.contentDuration * seekTo).toLong())
    }

    fun playNext() {
        exoPlayer.seekToNext()
    }

    fun playPrevious() {
        exoPlayer.seekToPrevious()
    }
}
