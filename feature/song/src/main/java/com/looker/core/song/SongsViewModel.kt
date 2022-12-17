package com.looker.core.song

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.looker.core.common.OrderType
import com.looker.core.common.combineAndStateIn
import com.looker.core.common.order.SongOrder
import com.looker.core.common.result.Result
import com.looker.core.common.result.asResult
import com.looker.core.data.repository.BlacklistsRepository
import com.looker.core.data.repository.SongsRepository
import com.looker.core.data.use_case.sortBy
import com.looker.core.service.MusicServiceConnection
import com.looker.core.service.extensions.playPauseMedia
import com.looker.core.ui.components.SongListUiState
import com.looker.core.ui.components.SongUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SongsViewModel @Inject constructor(
	private val musicServiceConnection: MusicServiceConnection,
	songsRepository: SongsRepository,
	blacklistsRepository: BlacklistsRepository
) : ViewModel() {

	private val songsStream = songsRepository.getSongsStream().asResult()

	init {
		viewModelScope.launch {
			songsStream.collectLatest {
				if (it is Result.Success && it.data.isEmpty())
					songsRepository.syncData()
			}
		}
	}

	private val _songOrder = MutableStateFlow<SongOrder>(SongOrder.Title(OrderType.Ascending))
	private val songOrder = _songOrder.asStateFlow()

	val songsState = combineAndStateIn(
		songsStream,
		songOrder,
		blacklistsRepository.getBlacklistSongs(),
		scope = viewModelScope,
		initialValue = SongListUiState(SongUiState.Loading)
	) { songsResult, songOrder, blackList ->
		val blacklistedSongs = blackList.flatMap { it.songs }
		val blacklistedAlbums = blackList.flatMap { it.songsFromAlbum }
		val songs = when (songsResult) {
			is Result.Error -> SongUiState.Error
			Result.Loading -> SongUiState.Loading
			is Result.Success -> SongUiState.Success(
				songsResult.data.filterNot {
					it.mediaId in blacklistedSongs || it.albumId.toString() in blacklistedAlbums
				}.sortBy(songOrder),
				songOrder
			)
		}
		SongListUiState(songs)
	}

	fun onEvent(event: SongEvent) {
		when (event) {
			is SongEvent.Order -> {
				viewModelScope.launch {
					_songOrder.emit(event.songOrder)
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