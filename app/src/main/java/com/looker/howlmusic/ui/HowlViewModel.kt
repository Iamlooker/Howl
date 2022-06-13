package com.looker.howlmusic.ui

import android.util.Log
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Shuffle
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.looker.components.state.SheetsState
import com.looker.components.state.SheetsState.HIDDEN
import com.looker.components.state.SheetsState.VISIBLE
import com.looker.constants.Constants.MEDIA_ROOT_ID
import com.looker.constants.states.ToggleState
import com.looker.constants.states.ToggleState.PlayControl
import com.looker.constants.states.ToggleState.Shuffle
import com.looker.core_model.Song
import com.looker.feature_player.service.MusicServiceConnection
import com.looker.feature_player.utils.ShuffleMode.SHUFFLE_MODE_ALL
import com.looker.feature_player.utils.ShuffleMode.SHUFFLE_MODE_NONE
import com.looker.feature_player.utils.extension.isPlayEnabled
import com.looker.feature_player.utils.extension.isPlaying
import com.looker.feature_player.utils.extension.isPrepared
import com.looker.feature_player.utils.extension.toSong
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class HowlViewModel
@Inject constructor(
	private val musicServiceConnection: MusicServiceConnection
) : ViewModel() {

	val backdropValue = MutableStateFlow<SheetsState>(HIDDEN)

	val playIcon = musicServiceConnection.playIcon
	val isPlaying = musicServiceConnection.isPlaying

	private val nowPlaying = musicServiceConnection.nowPlaying
	private val isShuffling = musicServiceConnection.shuffleMode

	val toggleStream =
		combine(
			isShuffling,
			isPlaying,
			backdropValue,
			musicServiceConnection.playIcon
		) { shuffling, playing, backdrop, icon ->
			when (backdrop) {
				VISIBLE -> ToggleButtonState(Shuffle, shuffling, Icons.Rounded.Shuffle)
				HIDDEN -> ToggleButtonState(PlayControl, playing, icon)
			}
		}.stateIn(
			scope = viewModelScope,
			started = SharingStarted.WhileSubscribed(5000),
			initialValue = ToggleButtonState(PlayControl, false)
		)

	init {
		musicServiceConnection.subscribe(MEDIA_ROOT_ID)
	}

	fun onToggleClick() {
		when (toggleStream.value.toggleState) {
			PlayControl -> playMedia(nowPlaying.value.toSong)
			Shuffle -> shuffleModeToggle()
		}
	}

	private fun playMedia(mediaItem: Song, pauseAllowed: Boolean = true) {
		val transportControls = musicServiceConnection.transportControls
		val isPrepared = musicServiceConnection.playbackState.value.isPrepared
		musicServiceConnection.playbackState.value.let { playbackState ->
			if (isPrepared) {
				when {
					playbackState.isPlaying -> if (pauseAllowed) transportControls.pause() else Unit
					playbackState.isPlayEnabled -> transportControls.play()
					else -> {
						Log.w(
							"HowlViewModel",
							"Playable item clicked but neither play nor pause are enabled!" +
									" (mediaId=${mediaItem.mediaId})"
						)
					}
				}
			}
		}
	}

	private fun shuffleModeToggle() {
		val transportControls = musicServiceConnection.transportControls
		transportControls.setShuffleMode(if (isShuffling.value) SHUFFLE_MODE_NONE else SHUFFLE_MODE_ALL)
	}

	override fun onCleared() {
		super.onCleared()
		musicServiceConnection.unsubscribe(MEDIA_ROOT_ID)
	}
}

data class ToggleButtonState(
	val toggleState: ToggleState,
	val enabled: Boolean,
	val icon: ImageVector = Icons.Rounded.PlayArrow
)