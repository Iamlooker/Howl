package com.looker.howlmusic

import androidx.compose.material.BackdropValue
import androidx.compose.material.BackdropValue.Concealed
import androidx.compose.material.BackdropValue.Revealed
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Shuffle
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.looker.components.state.PlayState
import com.looker.components.state.PlayState.PAUSED
import com.looker.components.state.PlayState.PLAYING
import com.looker.components.state.SheetsState
import com.looker.components.state.SheetsState.HIDDEN
import com.looker.components.state.SheetsState.VISIBLE
import com.looker.data_music.data.AlbumsRepository
import com.looker.data_music.data.SongsRepository
import com.looker.domain_music.Album
import com.looker.domain_music.Song
import com.looker.domain_music.emptySong
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HowlViewModel
@Inject constructor(
    private val exoPlayer: SimpleExoPlayer,
    private val songsRepository: SongsRepository,
    private val albumsRepository: AlbumsRepository,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
) : ViewModel() {

    private val _songsList = MutableStateFlow(emptyList<Song>())
    val songsList: StateFlow<List<Song>> = _songsList

    private val _albumsList = MutableStateFlow(emptyList<Album>())
    val albumsList: StateFlow<List<Album>> = _albumsList

    init {
        viewModelScope.launch(Dispatchers.Default) {
            songsRepository.getAllSongs()
                .collect { list ->
                    _songsList.value = list
                }
        }
        viewModelScope.launch(Dispatchers.Default) {
            albumsRepository.getAllAlbums()
                .collect {
                    _albumsList.value = it
                }
        }
    }

    private val _progress = MutableStateFlow(0f)
    private val _handleIcon = MutableStateFlow(0.5f)
    private val _currentIndex = MutableStateFlow(0)
    private val _enableGesture = MutableStateFlow(true)
    private val _playIcon = MutableStateFlow(Icons.Rounded.PlayArrow)
    private val _toggleIcon = MutableStateFlow(Icons.Rounded.Shuffle)
    private val _backdropValue = MutableStateFlow<SheetsState>(HIDDEN)
    private val _currentSong = MutableStateFlow(emptySong)
    private val _playState = MutableStateFlow<PlayState>(PAUSED)
    private val _toggle = MutableStateFlow(false)

    val progress: StateFlow<Float> = _progress
    val handleIcon: StateFlow<Float> = _handleIcon
    private val currentIndex: StateFlow<Int> = _currentIndex
    val currentSong: StateFlow<Song> = _currentSong
    val toggleIcon: StateFlow<ImageVector> = _toggleIcon
    val enableGesture: StateFlow<Boolean> = _enableGesture
    private val playIcon: StateFlow<ImageVector> = _playIcon
    val backdropValue: StateFlow<SheetsState> = _backdropValue
    val playState: StateFlow<PlayState> = _playState
    val toggle: StateFlow<Boolean> = _toggle

    @ExperimentalMaterialApi
    fun setBackdropValue(currentValue: BackdropValue) {
        viewModelScope.launch(ioDispatcher) {
            _backdropValue.value = when (currentValue) {
                Concealed -> HIDDEN
                Revealed -> VISIBLE
            }
        }
    }

    fun updateToggle() {
        viewModelScope.launch(ioDispatcher) {
            _toggle.value = when (backdropValue.value) {
                HIDDEN -> when (playState.value) {
                    PAUSED -> false
                    PLAYING -> true
                }
                VISIBLE -> false
            }
        }
    }

    fun gestureState(allowGesture: Boolean) {
        _enableGesture.value = allowGesture
    }

    private fun setPlayState(isPlaying: Boolean) {
        _playState.value = if (isPlaying) PLAYING else PAUSED
    }

    private fun setPlayState(isPlaying: PlayState) {
        _playState.value = isPlaying
    }

    private fun updatePlayIcon() {
        _playIcon.value = when (playState.value) {
            is PAUSED -> Icons.Rounded.PlayArrow
            is PLAYING -> Icons.Rounded.Pause
        }
    }

    fun onPlayPause(isPlaying: PlayState) {
        viewModelScope.launch(ioDispatcher) { setPlayState(isPlaying) }
        viewModelScope.launch(Dispatchers.Main) {
            playPause(playState.value)
            setPlayState(exoPlayer.isPlaying)
            updatePlayIcon()
        }
    }

    fun onToggle(currentState: SheetsState, playState: PlayState) = when (currentState) {
        is HIDDEN -> {
            onPlayPause(playState)
        }
        is VISIBLE -> {
        }
    }

    fun setToggleIcon(currentState: SheetsState) {
        viewModelScope.launch(ioDispatcher) {
            _toggleIcon.value = when (currentState) {
                is HIDDEN -> playIcon.value
                is VISIBLE -> Icons.Rounded.Shuffle
            }
        }
    }

    fun setHandleIcon(currentState: SheetsState) {
        viewModelScope.launch(ioDispatcher) {
            _handleIcon.value = when (currentState) {
                is HIDDEN -> 1f
                is VISIBLE -> 0f
            }
        }
    }

    fun onSongClicked(index: Int) {
        viewModelScope.launch(ioDispatcher) {
            launch { setPlayState(true) }.join()
            launch { updatePlayIcon() }
        }
        viewModelScope.launch(ioDispatcher) { setCurrentIndex(index) }
        viewModelScope.launch(ioDispatcher) { setCurrentSong(songsList.value[index]) }
        viewModelScope.launch(Dispatchers.Main) {
            exoPlayer.apply {
                clearMediaItems()
                setMediaItems(index, songsList.value)
                prepare()
                play()
            }
        }
    }

    private suspend fun SimpleExoPlayer.setMediaItems(index: Int, songs: List<Song>) {
        withContext(ioDispatcher) {
            val mediaItems = arrayListOf<MediaItem>()
            launch { songs.forEach { mediaItems.add(MediaItem.fromUri(it.songUri)) } }
            launch(Dispatchers.Main) { this@setMediaItems.setMediaItems(mediaItems, index, 0) }
        }
    }

    private fun playPause(playing: PlayState) {
        when (playing) {
            is PLAYING -> exoPlayer.pause()
            is PAUSED -> exoPlayer.playWhenReady = true
        }
    }

    fun onSeek(seekTo: Float) {
        viewModelScope.launch(ioDispatcher) { _progress.value = seekTo }
        viewModelScope.launch(Dispatchers.Main) { exoPlayer.seekTo((exoPlayer.contentDuration * seekTo).toLong()) }
    }

    fun playNext() {
        if (exoPlayer.hasNextWindow()) {
            exoPlayer.seekToNext()
            viewModelScope.launch { setCurrentIndex(_currentIndex.value.inc()) }
            viewModelScope.launch { setCurrentSong(songsList.value[currentIndex.value]) }
        }
    }

    fun playPrevious() {
        if (exoPlayer.hasPreviousWindow()) {
            exoPlayer.seekToPrevious()
            viewModelScope.launch { setCurrentIndex(_currentIndex.value.dec()) }
            viewModelScope.launch { setCurrentSong(songsList.value[currentIndex.value]) }
        }
    }

    private fun setCurrentSong(song: Song) {
        _currentSong.value = song
    }

    private fun setCurrentIndex(index: Int) {
        _currentIndex.value = index
    }

    override fun onCleared() {
        super.onCleared()
        exoPlayer.release()
    }
}