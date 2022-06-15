package com.looker.feature_player

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.looker.core_service.MusicService
import com.looker.core_service.MusicServiceConnection
import com.looker.core_service.utils.extension.currentPlaybackPosition
import com.looker.core_service.utils.extension.isPlayEnabled
import com.looker.core_service.utils.extension.isPlaying
import com.looker.core_service.utils.extension.isPrepared
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlayerViewModel
@Inject constructor(
	private val musicServiceConnection: MusicServiceConnection
) : ViewModel() {

	init {
		updateCurrentPlayerPosition()
	}

	private val _songDuration = MutableStateFlow(0L)
	private val _progress = MutableStateFlow(0F)
	val progress = _progress.asStateFlow()

	private val playbackState = musicServiceConnection.playbackState
	val isPlaying = musicServiceConnection.isPlaying
	val nowPlaying = musicServiceConnection.nowPlaying
	val playIcon = musicServiceConnection.playIcon

	fun playMedia() {
		val transportControls = musicServiceConnection.transportControls

		val isPrepared = playbackState.value.isPrepared
		playbackState.value.let { playbackState ->
			if (isPrepared) {
				when {
					playbackState.isPlaying -> transportControls.pause()
					playbackState.isPlayEnabled -> transportControls.play()
				}
			}
		}
	}

	fun onSeek(seekTo: Float) {
		viewModelScope.launch { _progress.emit(seekTo) }
	}

	fun onSeeked() {
		musicServiceConnection.transportControls.seekTo((_songDuration.value * progress.value).toLong())
	}

	fun playNext() {
		musicServiceConnection.transportControls.skipToNext()
	}

	fun playPrevious() {
		musicServiceConnection.transportControls.skipToPrevious()
	}

	private fun updateCurrentPlayerPosition() {
		viewModelScope.launch(Dispatchers.IO) {
			while (true) {
				val pos = playbackState.value.currentPlaybackPosition.toFloat()
				if (progress.value != pos) {
					_progress.emit(pos / MusicService.songDuration)
					_songDuration.emit(MusicService.songDuration)
				}
				delay(500)
			}
		}
	}
}