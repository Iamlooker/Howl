package com.looker.howlmusic.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Shuffle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.looker.components.state.SheetsState
import com.looker.components.state.SheetsState.HIDDEN
import com.looker.components.state.SheetsState.VISIBLE
import com.looker.core_common.Constants.MEDIA_ROOT_ID
import com.looker.core_common.states.ToggleButtonState
import com.looker.core_common.states.ToggleState.PlayControl
import com.looker.core_common.states.ToggleState.Shuffle
import com.looker.core_service.MusicServiceConnection
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
	val isPlaying = musicServiceConnection.isPlaying

	private val isShuffling = musicServiceConnection.shuffleMode

	val toggleStream =
		combine(
			isShuffling,
			isPlaying,
			backdropValue,
			musicServiceConnection.playIcon
		) { shuffling, playing, backdrop, playIcon ->
			when (backdrop) {
				VISIBLE -> ToggleButtonState(Shuffle, shuffling, Icons.Rounded.Shuffle)
				HIDDEN -> ToggleButtonState(PlayControl, playing, playIcon)
			}
		}.stateIn(
			scope = viewModelScope,
			started = SharingStarted.WhileSubscribed(5000),
			initialValue = ToggleButtonState(PlayControl, false)
		)

	init {
		musicServiceConnection.subscribe(MEDIA_ROOT_ID)
	}

	override fun onCleared() {
		super.onCleared()
		musicServiceConnection.unsubscribe(MEDIA_ROOT_ID)
	}
}