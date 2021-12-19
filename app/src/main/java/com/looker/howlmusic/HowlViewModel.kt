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
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
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
import com.looker.domain_music.emptyAlbum
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
    private val exoPlayer: ExoPlayer,
    private val songsRepository: SongsRepository,
    private val albumsRepository: AlbumsRepository,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
) : ViewModel() {

    init {
        viewModelScope.launch(Dispatchers.Default) {
            launch { songsRepository.getAllSongs().collect { list -> _songsList.emit(list) } }
            launch { albumsRepository.getAllAlbums().collect { _albumsList.emit(it.distinct()) } }
        }
    }

    private val _songsList = MutableStateFlow(emptyList<Song>())
    val songsList: StateFlow<List<Song>> = _songsList

    private val _albumsList = MutableStateFlow(emptyList<Album>())
    val albumsList: StateFlow<List<Album>> = _albumsList

    private val _backdropValue = MutableStateFlow<SheetsState>(HIDDEN)
    private val _currentAlbum = MutableStateFlow(emptyAlbum)
    private val _currentIndex = MutableStateFlow(0)
    private val _currentSong = MutableStateFlow(emptySong)
    private val _enableGesture = MutableStateFlow(true)
    private val _handleIcon = MutableStateFlow(0.5f)
    private val _playIcon = MutableStateFlow(Icons.Rounded.PlayArrow)
    private val _playState = MutableStateFlow<PlayState>(PAUSED)
    private val _progress = MutableStateFlow(0f)
    private val _toggle = MutableStateFlow(false)
    private val _toggleIcon = MutableStateFlow(Icons.Rounded.Shuffle)

    val backdropValue: StateFlow<SheetsState> = _backdropValue
    val currentAlbum: StateFlow<Album> = _currentAlbum
    val currentSong: StateFlow<Song> = _currentSong
    val enableGesture: StateFlow<Boolean> = _enableGesture
    val handleIcon: StateFlow<Float> = _handleIcon
    val progress: StateFlow<Float> = _progress
    val playState: StateFlow<PlayState> = _playState
    val toggle: StateFlow<Boolean> = _toggle
    val toggleIcon: StateFlow<ImageVector> = _toggleIcon

    @ExperimentalMaterialApi
    fun setBackdropValue(currentValue: BackdropValue) {
        viewModelScope.launch(ioDispatcher) {
            _backdropValue.emit(
                when (currentValue) {
                    Concealed -> HIDDEN
                    Revealed -> VISIBLE
                }
            )
        }
    }

    fun updateToggle() {
        viewModelScope.launch(ioDispatcher) {
            _toggle.emit(
                when (backdropValue.value) {
                    HIDDEN -> when (playState.value) {
                        PAUSED -> false
                        PLAYING -> true
                    }
                    VISIBLE -> false
                }
            )
        }
    }

    fun gestureState(allowGesture: Boolean) {
        viewModelScope.launch { _enableGesture.emit(allowGesture) }
    }

    private suspend fun setPlayState(isPlaying: Boolean) {
        _playState.emit(if (isPlaying) PLAYING else PAUSED)
    }

    private suspend fun setPlayState(isPlaying: PlayState) {
        _playState.emit(isPlaying)
    }

    private suspend fun updatePlayIcon() {
        _playIcon.emit(
            when (playState.value) {
                is PAUSED -> Icons.Rounded.PlayArrow
                is PLAYING -> Icons.Rounded.Pause
            }
        )
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
        is HIDDEN -> onPlayPause(playState)
        is VISIBLE -> Unit
    }

    fun setToggleIcon(currentState: SheetsState) {
        viewModelScope.launch(ioDispatcher) {
            _playIcon.collect {
                _toggleIcon.emit(
                    when (currentState) {
                        is HIDDEN -> it
                        is VISIBLE -> Icons.Rounded.Shuffle
                    }
                )
            }
        }
    }

    fun setHandleIcon(currentState: SheetsState) {
        viewModelScope.launch(ioDispatcher) {
            _handleIcon.emit(
                when (currentState) {
                    is HIDDEN -> 1f
                    is VISIBLE -> 0f
                }
            )
        }
    }

    fun onSongClicked(index: Int) {
        viewModelScope.launch(ioDispatcher) {
            setPlayState(true)
            updatePlayIcon()
            launch { setCurrentIndex(index) }
        }
        viewModelScope.launch(Dispatchers.Main) {
            launch { setCurrentSong(songsList.value[index]) }
            exoPlayer.apply {
                clearMediaItems()
                setMediaItems(index)
                prepare()
                play()
            }
        }
    }

    fun onAlbumClick(index: Int) {
        viewModelScope.launch(ioDispatcher) {
            albumsList.collect { albums -> _currentAlbum.emit(albums[index]) }
        }
    }

    private suspend fun ExoPlayer.setMediaItems(index: Int) {
        withContext(ioDispatcher) {
            val mediaItems = arrayListOf<MediaItem>()
            launch { songsList.value.forEach { mediaItems.add(MediaItem.fromUri(it.songUri)) } }
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
        viewModelScope.launch(ioDispatcher) { _progress.emit(seekTo) }
        viewModelScope.launch(Dispatchers.Main) { exoPlayer.seekTo((exoPlayer.contentDuration * seekTo).toLong()) }
    }

    fun playNext() {
        if (exoPlayer.hasNextMediaItem()) {
            exoPlayer.seekToNext()
            viewModelScope.launch {
                launch { setCurrentIndex(_currentIndex.value.inc()) }
                launch {
                    songsList.collect { songs ->
                        _currentIndex.collect { index ->
                            setCurrentSong(songs[index])
                        }
                    }
                }
            }
        }
    }

    fun playPrevious() {
        if (exoPlayer.hasPreviousMediaItem()) {
            exoPlayer.seekToPrevious()
            viewModelScope.launch {
                launch { setCurrentIndex(_currentIndex.value.dec()) }
                launch {
                    songsList.collect { songs ->
                        _currentIndex.collect { index ->
                            setCurrentSong(songs[index])
                        }
                    }
                }
            }
        }
    }

    private suspend fun setCurrentSong(song: Song) = _currentSong.emit(song)

    private suspend fun setCurrentIndex(index: Int) = _currentIndex.emit(index)

    override fun onCleared() {
        super.onCleared()
        exoPlayer.release()
    }
}
