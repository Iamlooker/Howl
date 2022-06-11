package com.looker.feature_player.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.looker.core_model.Song
import com.looker.feature_player.service.MusicService
import com.looker.feature_player.service.MusicServiceConnection
import com.looker.feature_player.utils.extension.*
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

	private val playbackState = musicServiceConnection.playbackState
	private val _songDuration = MutableStateFlow(0L)
	private val _progress = MutableStateFlow(0F)

	val progress = _progress.asStateFlow()
	val isPlaying = musicServiceConnection.isPlaying
	val nowPlaying = musicServiceConnection.nowPlaying
	val playIcon = musicServiceConnection.playIcon

	fun playMedia(mediaItem: Song, pauseAllowed: Boolean = true) {
		val transportControls = musicServiceConnection.transportControls

		val isPrepared = playbackState.value.isPrepared
		playbackState.value.let { playbackState ->
			if (isPrepared && mediaItem.mediaId == nowPlaying.value.id) {
				when {
					playbackState.isPlaying -> if (pauseAllowed) transportControls.pause() else Unit
					playbackState.isPlayEnabled -> transportControls.play()
					else -> {
						Log.w(
							"PlayerViewModel",
							"Playable item clicked but neither play nor pause are enabled!" +
									" (mediaId=${mediaItem.mediaId})"
						)
					}
				}
			} else {
				if (mediaItem.mediaId.isNotEmpty()) transportControls.playFromMediaId(
					mediaItem.mediaId,
					null
				)
				else Unit
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