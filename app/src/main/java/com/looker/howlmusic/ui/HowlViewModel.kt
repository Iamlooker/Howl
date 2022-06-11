package com.looker.howlmusic.ui

import android.util.Log
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Shuffle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.looker.components.state.SheetsState
import com.looker.constants.Constants.MEDIA_ROOT_ID
import com.looker.core_model.Song
import com.looker.feature_player.service.MusicServiceConnection
import com.looker.feature_player.utils.ShuffleMode.SHUFFLE_MODE_ALL
import com.looker.feature_player.utils.ShuffleMode.SHUFFLE_MODE_NONE
import com.looker.feature_player.utils.extension.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HowlViewModel
@Inject constructor(
	private val musicServiceConnection: MusicServiceConnection
) : ViewModel() {

	val backdropValue = MutableStateFlow<SheetsState>(SheetsState.HIDDEN)

	val nowPlaying = musicServiceConnection.nowPlaying
	val playIcon = musicServiceConnection.playIcon
	val shuffleMode = musicServiceConnection.shuffleMode
	val isPlaying = musicServiceConnection.isPlaying
	val toggle = if (backdropValue.value == SheetsState.VISIBLE) shuffleMode else isPlaying

	private val _toggleIcon = MutableStateFlow(Icons.Rounded.Shuffle)
	val toggleIcon = _toggleIcon.asStateFlow()

	init {
		musicServiceConnection.subscribe(MEDIA_ROOT_ID)
	}

	fun updateToggleIcon() {
		viewModelScope.launch(Dispatchers.Default) {
			playIcon.collectLatest {
				_toggleIcon.value = when (backdropValue.value) {
					SheetsState.HIDDEN -> it
					SheetsState.VISIBLE -> Icons.Rounded.Shuffle
				}
			}
		}
	}

	fun onToggleClick() {
		when (backdropValue.value) {
			SheetsState.HIDDEN -> playMedia(nowPlaying.value.toSong)
			SheetsState.VISIBLE -> shuffleModeToggle()
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

	override fun onCleared() {
		super.onCleared()
		musicServiceConnection.unsubscribe(MEDIA_ROOT_ID)
	}
}