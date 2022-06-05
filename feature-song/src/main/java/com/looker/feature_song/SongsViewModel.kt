package com.looker.feature_song

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.looker.core_common.result.Result
import com.looker.core_common.result.asResult
import com.looker.core_data.repository.SongsRepository
import com.looker.core_model.Song
import com.looker.feature_player.service.MusicServiceConnection
import com.looker.feature_player.utils.extension.isPrepared
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class SongsViewModel @Inject constructor(
	private val musicServiceConnection: MusicServiceConnection,
	songsRepository: SongsRepository
) : ViewModel() {

	private val songsStream: Flow<Result<List<Song>>> =
		songsRepository.getSongsStream().asResult()

	val songsState = songsStream.map { songsResult ->
		val songs = when (songsResult) {
			Result.Loading -> SongUiState.Loading
			is Result.Error -> SongUiState.Error
			is Result.Success -> SongUiState.Success(songsResult.data)
		}
		SongScreenUiState(songs)
	}.stateIn(
		scope = viewModelScope,
		started = SharingStarted.WhileSubscribed(5000),
		initialValue = SongScreenUiState(SongUiState.Loading)
	)

	fun playSong(song: Song) {
		val transportControls = musicServiceConnection.transportControls
		val isPrepared = musicServiceConnection.playbackState.value.isPrepared

		if (isPrepared && song.mediaId.isNotEmpty()) transportControls.playFromMediaId(
			song.mediaId,
			null
		)
	}
}

data class SongScreenUiState(val songsState: SongUiState)

sealed interface SongUiState {
	data class Success(val songs: List<Song>) : SongUiState
	object Error : SongUiState
	object Loading : SongUiState
}