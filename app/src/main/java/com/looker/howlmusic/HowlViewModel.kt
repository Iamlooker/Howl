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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HowlViewModel
@Inject constructor(
    private val exoPlayer: SimpleExoPlayer,
    private val songsRepository: SongsRepository,
    private val albumsRepository: AlbumsRepository
) : ViewModel() {

    private var currentPlaylist by mutableStateOf(emptyList<Song>())

    private val _songsList = MutableStateFlow(emptyList<Song>())
    val songsList: StateFlow<List<Song>> = _songsList

    private val _albumsList = MutableStateFlow(emptyList<Album>())
    val albumsList: StateFlow<List<Album>> = _albumsList

    private val _songsListSize = MutableStateFlow(0)
    private val songsListSize: StateFlow<Int> = _songsListSize

    init {
        viewModelScope.launch {
            songsRepository.getAllSongs()
                .collect { list ->
                    _songsList.value = list
                    _songsListSize.value = list.size
                }
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

    val progress: StateFlow<Float> = _progress
    val handleIcon: StateFlow<Float> = _handleIcon
    private val currentIndex: StateFlow<Int> = _currentIndex
    val currentSong: StateFlow<Song> = _currentSong
    val toggleIcon: StateFlow<ImageVector> = _toggleIcon
    val enableGesture: StateFlow<Boolean> = _enableGesture
    private val playIcon: StateFlow<ImageVector> = _playIcon
    val backdropValue: StateFlow<SheetsState> = _backdropValue
    val playState: StateFlow<PlayState> = _playState

    @ExperimentalMaterialApi
    fun setBackdropValue(currentValue: BackdropValue) {
        _backdropValue.value = when (currentValue) {
            Concealed -> HIDDEN
            Revealed -> VISIBLE
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

    private fun updatePlayIcon(isPlaying: PlayState) {
        _playIcon.value = when (isPlaying) {
            is PAUSED -> Icons.Rounded.PlayArrow
            is PLAYING -> Icons.Rounded.Pause
        }
    }

    fun onPlayPause(isPlaying: PlayState) {
        viewModelScope.launch {
            setPlayState(isPlaying)
            playPause(playState.value)
            setPlayState(exoPlayer.isPlaying)
            updatePlayIcon(playState.value)
        }
    }

    fun onToggle(currentState: SheetsState, playState: PlayState) = when (currentState) {
        is HIDDEN -> {
            onPlayPause(playState)
        }
        is VISIBLE -> {
            onPlayPause(playState)
        }
    }

    fun setToggleIcon(currentState: SheetsState) {
        _toggleIcon.value = when (currentState) {
            is HIDDEN -> playIcon.value
            is VISIBLE -> Icons.Rounded.Shuffle
        }
    }

    fun setHandleIcon(currentState: SheetsState) {
        _handleIcon.value = when (currentState) {
            is HIDDEN -> 1f
            is VISIBLE -> 0f
        }
    }

    fun onSongClicked(index: Int) {
        setPlayState(true)
        updatePlayIcon(playState.value)
        currentPlaylist = songsList.value.subList(index, songsListSize.value - 1)
        setCurrentIndex(index)
        setCurrentSong(songsList.value[index])
        exoPlayer.apply {
            clearMediaItems()
            setMediaItems(currentPlaylist)
            prepare()
            play()
        }
    }

    private fun SimpleExoPlayer.setMediaItems(songs: List<Song>) {
        val mediaItems = arrayListOf<MediaItem>()
        songs.forEach { mediaItems.add(MediaItem.fromUri(it.songUri)) }
        this.setMediaItems(mediaItems, true)
    }

    private fun playPause(playing: PlayState) {
        when (playing) {
            is PLAYING -> exoPlayer.pause()
            is PAUSED -> exoPlayer.playWhenReady = true
        }
    }

    fun onSeek(seekTo: Float) {
        _progress.value = seekTo
        exoPlayer.seekTo((exoPlayer.contentDuration * seekTo).toLong())
    }

    fun playNext() {
        if (exoPlayer.hasNextWindow()) {
            exoPlayer.seekToNext()
            setCurrentIndex(_currentIndex.value.inc())
            setCurrentSong(songsList.value[currentIndex.value])
        }
    }

    fun playPrevious() {
        if (exoPlayer.hasPreviousWindow()) {
            exoPlayer.seekToPrevious()
            setCurrentIndex(_currentIndex.value.dec())
            setCurrentSong(songsList.value[currentIndex.value])
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
