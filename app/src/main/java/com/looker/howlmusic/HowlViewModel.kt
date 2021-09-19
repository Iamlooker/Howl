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
import com.looker.player_service.playback.Controls.clearList
import com.looker.player_service.playback.Controls.playNextSong
import com.looker.player_service.playback.Controls.playPauseSong
import com.looker.player_service.playback.Controls.playPreviousSong
import com.looker.player_service.playback.Controls.playSong
import com.looker.player_service.playback.Controls.prepareSong
import com.looker.player_service.playback.Controls.seekToFloat
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HowlViewModel
@Inject constructor(
    private val player: SimpleExoPlayer
) : ViewModel() {

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
    val playIcon: LiveData<ImageVector> = _playIcon

    fun updatePlayIcon() {
        _playIcon.value =
            if (_playing.value == true) Icons.Rounded.Pause else Icons.Rounded.PlayArrow
    }

    fun onPlayPause() {
        player.playPauseSong()
        _playing.value = player.isPlaying
        updatePlayIcon()
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

    fun onSongClicked(song: Song) {
        _playing.value = true
        _currentSong.value = song
        player.apply {
            clearList()
            prepareSong(song.songUri)
            playSong()
        }
        updatePlayIcon()
    }

    fun onSeek(seekTo: Float) {
        _progress.value = seekTo
        player.seekToFloat(seekTo)
    }

    fun playNext() {
        player.playNextSong()
    }

    fun playPrevious() {
        player.playPreviousSong()
    }

    @ExperimentalMaterialApi
    fun playerVisible(state: BackdropScaffoldState): Boolean = state.isRevealed

    override fun onCleared() {
        super.onCleared()
        player.release()
    }
}
