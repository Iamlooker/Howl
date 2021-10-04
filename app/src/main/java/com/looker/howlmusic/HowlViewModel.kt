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
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.looker.components.SheetsState
import com.looker.domain_music.Song
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HowlViewModel
@Inject constructor(private val exoPlayer: SimpleExoPlayer) : ViewModel() {

    private var currentPlaylist by mutableStateOf(emptyList<Song>())

    private val _currentIndex = MutableStateFlow(0)
    private val _playIcon = MutableStateFlow(Icons.Rounded.PlayArrow)
    private val _playing = MutableStateFlow(false)
    private val _enableGesture = MutableStateFlow(true)
    private val _toggleIcon = MutableStateFlow(Icons.Rounded.Shuffle)
    private val _handleIcon = MutableStateFlow(2f)
    private val _progress = MutableStateFlow(0f)
    private val _backdropValue = MutableStateFlow<SheetsState>(SheetsState.HIDDEN)
    private val _currentSong = MutableStateFlow(
        Song(
            songUri = "",
            albumId = 0,
            genreId = 0,
            songName = null,
            artistName = null,
            albumName = null,
            albumArt = ""
        )
    )

    private val currentIndex: StateFlow<Int> = _currentIndex
    private val playIcon: StateFlow<ImageVector> = _playIcon
    val playing: StateFlow<Boolean> = _playing
    val enableGesture: StateFlow<Boolean> = _enableGesture
    val toggleIcon: StateFlow<ImageVector> = _toggleIcon
    val handleIcon: StateFlow<Float> = _handleIcon
    val progress: StateFlow<Float> = _progress
    val backdropValue: StateFlow<SheetsState> = _backdropValue
    val currentSong: StateFlow<Song> = _currentSong

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

    fun onPlayPause() {
        viewModelScope.launch {
            playPause(_playing.value)
            _playing.value = exoPlayer.isPlaying
            _playIcon.value = if (_playing.value) Icons.Rounded.Pause
            else Icons.Rounded.PlayArrow
        }
    }

    fun onToggle(currentState: SheetsState) = when (currentState) {
        SheetsState.HIDDEN -> onPlayPause()
        SheetsState.TO_HIDDEN -> onPlayPause()
        else -> {
        }
    }

    fun setToggleIcon(currentState: SheetsState) {
        _toggleIcon.value = when (currentState) {
            SheetsState.TO_HIDDEN -> playIcon.value
            SheetsState.TO_VISIBLE -> Icons.Rounded.Shuffle
            SheetsState.HIDDEN -> playIcon.value
            SheetsState.VISIBLE -> Icons.Rounded.Shuffle
        }
    }

    fun setHandleIcon(currentState: SheetsState) {
        _handleIcon.value = when (currentState) {
            SheetsState.TO_HIDDEN -> 1f
            SheetsState.TO_VISIBLE -> 1f
            SheetsState.HIDDEN -> 0f
            SheetsState.VISIBLE -> 2f
        }
    }

    fun onSongClicked(songs: List<Song>) {
        _playing.value = true
        _playIcon.value = Icons.Rounded.Pause
        _currentSong.value = songs[currentIndex.value]
        currentPlaylist = songs
        exoPlayer.apply {
            clearMediaItems()
            setMediaItems(currentPlaylist)
            prepare()
            play()
        }
    }

    private fun SimpleExoPlayer.setMediaItems(songs: List<Song>) {

        val mediaItems = arrayListOf<MediaItem>()
        songs.forEach {
            mediaItems.add(MediaItem.fromUri(it.songUri))
        }
        this.setMediaItems(mediaItems, true)
    }

    private suspend fun playPause(playing: Boolean) {
        withContext(Dispatchers.Main) {
            launch {
                if (playing) exoPlayer.pause()
                else exoPlayer.playWhenReady = true
            }
        }
    }

    fun onSeek(seekTo: Float) {
        _progress.value = seekTo
        exoPlayer.seekTo((exoPlayer.contentDuration * seekTo).toLong())
    }

    fun playNext() {
        if (exoPlayer.hasNextWindow()) {
            exoPlayer.seekToNext()
            _currentIndex.value += 1
            _currentSong.value = currentPlaylist[currentIndex.value]
        }
    }

    fun playPrevious() {
        if (exoPlayer.hasPreviousWindow()) {
            exoPlayer.seekToPrevious()
            _currentIndex.value -= 1
            _currentSong.value = currentPlaylist[currentIndex.value]
        }
    }
}
