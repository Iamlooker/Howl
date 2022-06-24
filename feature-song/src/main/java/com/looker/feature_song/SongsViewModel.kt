package com.looker.feature_song

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.looker.core_common.combineAndStateIn
import com.looker.core_common.result.Result
import com.looker.core_common.result.asResult
import com.looker.core_data.repository.BlacklistsRepository
import com.looker.core_data.repository.SongsRepository
import com.looker.core_model.Song
import com.looker.core_service.MusicServiceConnection
import com.looker.core_service.extensions.playPauseMedia
import com.looker.core_ui.SongListUiState
import com.looker.core_ui.SongUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SongsViewModel @Inject constructor(
	private val musicServiceConnection: MusicServiceConnection,
	songsRepository: SongsRepository,
	blacklistsRepository: BlacklistsRepository
) : ViewModel() {

	private val songsStream: Flow<Result<List<Song>>> =
		songsRepository.getSongsStream().asResult()

	init {
		viewModelScope.launch {
			songsStream.collectLatest {
				if (it is Result.Success && it.data.isEmpty())
					songsRepository.syncData()
			}
		}
	}

	val songsState = combineAndStateIn(
		songsStream,
		blacklistsRepository.getBlacklistSongs(),
		scope = viewModelScope,
		initialValue = SongListUiState(SongUiState.Loading)
	) { songsResult, blacklist ->
		val blacklistedSongs = blacklist.flatMap { it.songsFromAlbum }
		val songs = when (songsResult) {
			Result.Loading -> SongUiState.Loading
			is Result.Error -> SongUiState.Error
			is Result.Success -> SongUiState.Success(
				songsResult.data.filterNot { it.albumId.toString() in blacklistedSongs }
			)
		}
		SongListUiState(songs)
	}

	fun playSong(song: Song) {
		song.playPauseMedia(
			musicServiceConnection = musicServiceConnection,
			canPause = false
		)
	}
}