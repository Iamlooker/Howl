package com.looker.feature_player

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Shuffle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.looker.core_common.states.SheetsState
import com.looker.core_common.states.SheetsState.HIDDEN
import com.looker.core_common.states.SheetsState.VISIBLE
import com.looker.core_common.states.ToggleButtonState
import com.looker.core_common.states.ToggleState
import com.looker.core_service.MusicServiceConnection
import com.looker.core_service.utils.SHUFFLE_MODE_ALL
import com.looker.core_service.utils.SHUFFLE_MODE_NONE
import com.looker.core_service.utils.ShuffleMode
import com.looker.core_service.utils.extension.currentPlaybackPosition
import com.looker.core_service.utils.extension.playPauseMedia
import com.looker.core_service.utils.extension.toSong
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlayerViewModel
@Inject constructor(
	musicServiceConnection: MusicServiceConnection
) : ViewModel() {

	private val musicServiceConnection = musicServiceConnection.also {
		updateCurrentPlayerPosition()
	}

	private val backdropValue = MutableStateFlow<SheetsState>(HIDDEN)
	private val _songDuration = MutableStateFlow(0L)
	private val _progress = MutableStateFlow(0F)
	val progress = _progress.asStateFlow()

	private val playbackState = musicServiceConnection.playbackState
	private val isShuffling = musicServiceConnection.shuffleMode
	val isPlaying = musicServiceConnection.isPlaying
	val nowPlaying = musicServiceConnection.nowPlaying
	val playIcon = musicServiceConnection.playIcon

	fun playMedia() {
		nowPlaying.value.playPauseMedia(
			musicServiceConnection = musicServiceConnection,
			playNewOrOld = { true }
		)
	}

	fun setBackdrop(state: SheetsState) {
		backdropValue.value = state
	}

	val toggleStream =
		combine(
			isShuffling,
			isPlaying,
			backdropValue,
			playIcon
		) { shuffling, playing, backdrop, playIcon ->
			when (backdrop) {
				VISIBLE -> ToggleButtonState(ToggleState.Shuffle, shuffling, Icons.Rounded.Shuffle)
				HIDDEN -> ToggleButtonState(ToggleState.PlayControl, playing, playIcon)
			}
		}.stateIn(
			scope = viewModelScope,
			started = SharingStarted.WhileSubscribed(5000),
			initialValue = ToggleButtonState(ToggleState.PlayControl, false)
		)

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

	fun onToggleClick(toggleState: ToggleState) {
		when (toggleState) {
			ToggleState.PlayControl -> playMedia()
			ToggleState.Shuffle -> shuffleModeToggle()
		}
	}

	private fun shuffleModeToggle() {
		val transportControls = musicServiceConnection.transportControls
		@ShuffleMode
		transportControls.setShuffleMode(if (isShuffling.value) SHUFFLE_MODE_NONE else SHUFFLE_MODE_ALL)
	}

	private fun updateCurrentPlayerPosition() {
		viewModelScope.launch(Dispatchers.IO) {
			while (true) {
				val pos = playbackState.value.currentPlaybackPosition
				val songDuration = nowPlaying.value.toSong.duration
				if (progress.value != pos && songDuration > 0) {
					_progress.emit(pos / songDuration)
					_songDuration.emit(songDuration)
				}
				delay(POSITION_UPDATE_INTERVAL_MILLIS)
			}
		}
	}
}

private const val POSITION_UPDATE_INTERVAL_MILLIS = 100L