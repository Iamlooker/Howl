package com.looker.feature_song

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.looker.core_common.mapAndStateIn
import com.looker.core_common.result.Result
import com.looker.core_common.result.asResult
import com.looker.core_data.repository.SongsRepository
import com.looker.core_model.Song
import com.looker.core_service.MusicServiceConnection
import com.looker.core_service.utils.extension.playPauseMedia
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SongsViewModel @Inject constructor(
	private val musicServiceConnection: MusicServiceConnection,
	songsRepository: SongsRepository
) : ViewModel() {

	init {
		viewModelScope.launch { songsRepository.syncData() }
	}

	private val songsStream: Flow<Result<List<Song>>> =
		songsRepository.getSongsStream().asResult()

	val songsState = songsStream.mapAndStateIn(
		scope = viewModelScope,
		initialValue = SongScreenUiState(SongUiState.Loading)
	) { songsResult ->
		val songs = when (songsResult) {
			Result.Loading -> SongUiState.Loading
			is Result.Error -> SongUiState.Error
			is Result.Success -> SongUiState.Success(songsResult.data)
		}
		SongScreenUiState(songs)
	}

	fun playSong(song: Song) {
		song.playPauseMedia(
			musicServiceConnection = musicServiceConnection,
			canPause = false
		)
	}
}

@Immutable
data class SongScreenUiState(val songsState: SongUiState)

sealed interface SongUiState {
	data class Success(val songs: List<Song>) : SongUiState
	object Error : SongUiState
	object Loading : SongUiState
}