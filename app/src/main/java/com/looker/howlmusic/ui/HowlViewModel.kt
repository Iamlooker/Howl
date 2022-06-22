package com.looker.howlmusic.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.looker.core_common.Constants.MEDIA_ROOT_ID
import com.looker.core_common.states.SheetsState
import com.looker.core_common.states.SheetsState.HIDDEN
import com.looker.core_service.MusicServiceConnection
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HowlViewModel
@Inject constructor(
	private val musicServiceConnection: MusicServiceConnection
) : ViewModel() {

	init {
		musicServiceConnection.subscribe(MEDIA_ROOT_ID)
	}

	val isPlaying = musicServiceConnection.isPlaying

	private val _backdropValue = MutableStateFlow<SheetsState>(HIDDEN)
	val backdropValue = _backdropValue.asStateFlow()

	fun setBackDrop(sheetsState: SheetsState) =
		viewModelScope.launch { _backdropValue.emit(sheetsState) }

	override fun onCleared() {
		super.onCleared()
		musicServiceConnection.unsubscribe(MEDIA_ROOT_ID)
	}
}