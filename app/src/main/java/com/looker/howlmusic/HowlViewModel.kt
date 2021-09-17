package com.looker.howlmusic

import androidx.compose.material.BackdropScaffoldState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.material.icons.rounded.ArrowDropUp
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.exoplayer2.SimpleExoPlayer
import com.looker.domain_music.Song
import com.looker.player_service.service.PlayerService

class HowlViewModel : ViewModel() {

    lateinit var player: SimpleExoPlayer

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

    @ExperimentalMaterialApi
    fun playerVisible(state: BackdropScaffoldState): Boolean = state.isRevealed

    fun onPlayPause() {
        if (player.isPlaying) player.pause()
        else player.play()
        _playing.value = player.isPlaying
    }

    fun onToggle(playerVisible: Boolean) {
        if (playerVisible) {
            player.shuffleModeEnabled = _shuffle.value ?: false
        } else onPlayPause()
    }

    fun setHandleIcon(playerVisible: Boolean) {
        _handleIcon.value = if (playerVisible) Icons.Rounded.ArrowDropUp
        else Icons.Rounded.ArrowDropDown
    }

    fun onSongClicked(playerService: PlayerService, song: Song) {
        _playing.value = true
        _currentSong.value = song
        playerService.playSong(song.songUri)
    }

    fun onSeek(seekTo: Float) {
        _progress.value = seekTo
        seekSongTo(seekTo)
    }

    fun seekSongTo(progress: Float) {
        val long: Long? = (_currentSong.value?.duration?.times(progress))?.toLong()
        player.seekTo(long ?: 0)
    }

    fun playNext() {
        if (player.hasNextWindow()) player.seekToNext()
    }

    fun playPrevious() {
        if (player.hasPreviousWindow()) player.seekToPrevious()
    }

    override fun onCleared() {
        super.onCleared()
        player.release()
    }
}