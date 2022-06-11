package com.looker.howlmusic.ui

import android.util.Log
import androidx.compose.animation.core.TweenSpec
import androidx.compose.material.BackdropScaffoldState
import androidx.compose.material.BackdropValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.looker.components.state.SheetsState
import com.looker.constants.Constants.MEDIA_ROOT_ID
import com.looker.constants.states.ToggleButtonState
import com.looker.constants.states.ToggleState
import com.looker.core_model.Song
import com.looker.feature_player.service.MusicServiceConnection
import com.looker.feature_player.utils.ShuffleMode.SHUFFLE_MODE_ALL
import com.looker.feature_player.utils.ShuffleMode.SHUFFLE_MODE_NONE
import com.looker.feature_player.utils.extension.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HowlViewModel
@Inject constructor(
	private val musicServiceConnection: MusicServiceConnection
) : ViewModel() {

	val backdropValue = MutableStateFlow<SheetsState>(SheetsState.HIDDEN)

	private val nowPlaying = musicServiceConnection.nowPlaying
	val playIcon = musicServiceConnection.playIcon
	private val shuffleMode = musicServiceConnection.shuffleMode

	val isPlaying = musicServiceConnection.isPlaying

	val toggleStream =
		combine(shuffleMode, isPlaying, backdropValue) { shuffling, playing, backdrop ->
			when (backdrop) {
				SheetsState.VISIBLE -> ToggleButtonState(ToggleState.Shuffle, shuffling)
				SheetsState.HIDDEN -> ToggleButtonState(ToggleState.PlayControl, playing)
			}
		}.stateIn(
			scope = viewModelScope,
			started = SharingStarted.WhileSubscribed(5000),
			initialValue = ToggleButtonState(ToggleState.PlayControl, false)
		)

	init {
		musicServiceConnection.subscribe(MEDIA_ROOT_ID)
	}

	fun onToggleClick() {
		when (toggleStream.value.toggleState) {
			ToggleState.PlayControl -> playMedia(nowPlaying.value.toSong)
			ToggleState.Shuffle -> shuffleModeToggle()
		}
	}

	private fun playMedia(mediaItem: Song, pauseAllowed: Boolean = true) {
		val nowPlaying = musicServiceConnection.nowPlaying.value
		val transportControls = musicServiceConnection.transportControls

		val isPrepared = musicServiceConnection.playbackState.value.isPrepared
		musicServiceConnection.playbackState.value.let { playbackState ->
			if (isPrepared && mediaItem.mediaId == nowPlaying.id) {
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
			} else {
				if (mediaItem.mediaId.isNotEmpty()) transportControls.playFromMediaId(
					mediaItem.mediaId,
					null
				)
				else Unit
			}
		}
	}

	private fun shuffleModeToggle() {
		val transportControls = musicServiceConnection.transportControls
		transportControls.setShuffleMode(if (shuffleMode.value) SHUFFLE_MODE_NONE else SHUFFLE_MODE_ALL)
	}

	@OptIn(ExperimentalMaterialApi::class)
	fun openPlayer(state: BackdropScaffoldState) {
		viewModelScope.launch {
			state.animateTo(BackdropValue.Revealed, TweenSpec(400))
		}
	}

	override fun onCleared() {
		super.onCleared()
		musicServiceConnection.unsubscribe(MEDIA_ROOT_ID)
	}
}