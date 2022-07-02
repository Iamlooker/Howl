package com.looker.feature_song

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.looker.core_common.OrderType
import com.looker.core_common.order.SongOrder
import com.looker.core_data.repository.BlacklistsRepository
import com.looker.core_data.repository.SongsRepository
import com.looker.core_data.use_case.GetSongs
import com.looker.core_service.MusicServiceConnection
import com.looker.core_service.extensions.playPauseMedia
import com.looker.core_ui.components.SongListUiState
import com.looker.core_ui.components.SongUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SongsViewModel @Inject constructor(
	private val musicServiceConnection: MusicServiceConnection,
	private val getSongs: GetSongs,
	songsRepository: SongsRepository,
	private val blacklistsRepository: BlacklistsRepository
) : ViewModel() {

	init {
		viewModelScope.launch {
			getSongs(SongOrder.Title(OrderType.Ascending)).collectLatest {
				songsRepository.syncData()
			}
		}
		getSong(SongOrder.Title(OrderType.Ascending))
	}

	private val _state = MutableStateFlow(SongListUiState(SongUiState.Loading))
	val state = _state.asStateFlow()

	private fun getSong(songOrder: SongOrder) {
		viewModelScope.launch {
			getSongs(songOrder).collectLatest { songs ->
				blacklistsRepository.getBlacklistSongs().collectLatest { blacklist ->
					val blacklistSongs = blacklist.flatMap { it.songsFromAlbum }
					_state.emit(
						SongListUiState(
							SongUiState.Success(
								songs = songs.filterNot { it.albumId.toString() in blacklistSongs },
								songOrder = songOrder
							)
						)
					)
				}
			}
		}
	}

	fun onEvent(event: SongEvent) {
		when (event) {
			is SongEvent.Order -> {
				viewModelScope.launch {
					getSong(event.songOrder)
				}
			}
			is SongEvent.PlayMedia -> {
				event.song.playPauseMedia(
					musicServiceConnection = musicServiceConnection,
					canPause = false
				)
			}
		}
	}
}