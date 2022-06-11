package com.looker.feature_album

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.looker.core_common.result.Result
import com.looker.core_common.result.asResult
import com.looker.core_data.repository.AlbumsRepository
import com.looker.core_model.Album
import com.looker.core_model.Song
import com.looker.feature_player.service.MusicServiceConnection
import com.looker.feature_player.utils.extension.isPrepared
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlbumsViewModel @Inject constructor(
	private val musicServiceConnection: MusicServiceConnection,
	albumsRepository: AlbumsRepository
) : ViewModel() {

	init { viewModelScope.launch { albumsRepository.syncData() } }

	private val _currentAlbum = MutableStateFlow(Album())
	val currentAlbum = _currentAlbum.asStateFlow()

	private val albumsStream: Flow<Result<List<Album>>> =
		albumsRepository.getAlbumsStream().asResult()

	private val songsStream: Flow<Result<List<Song>>> =
		albumsRepository.getRelatedSongs().asResult()

	val albumsState = albumsStream.map { albumsResult ->
		val albums = when (albumsResult) {
			Result.Loading -> AlbumUiState.Loading
			is Result.Error -> AlbumUiState.Error
			is Result.Success -> AlbumUiState.Success(albumsResult.data)
		}
		AlbumScreenUiState(albums)
	}.stateIn(
		scope = viewModelScope,
		started = SharingStarted.WhileSubscribed(5000),
		initialValue = AlbumScreenUiState(AlbumUiState.Loading)
	)

	val songsState = songsStream.combine(currentAlbum) { songsResult, currentAlbum ->
		val songs = when (songsResult) {
			Result.Loading -> SongUiState.Loading
			is Result.Error -> SongUiState.Error
			is Result.Success -> SongUiState.Success(
				currentAlbum.albumId,
				songsResult.data.filter { it.albumId == currentAlbum.albumId })
		}
		SongListUiState(songs)
	}.stateIn(
		scope = viewModelScope,
		started = SharingStarted.WhileSubscribed(5000),
		initialValue = SongListUiState(SongUiState.Loading)
	)

	fun setCurrentAlbum(newAlbum: Album) {
		viewModelScope.launch { _currentAlbum.emit(newAlbum) }
	}

	fun playSong(song: Song) {
		val transportControls = musicServiceConnection.transportControls
		val isPrepared = musicServiceConnection.playbackState.value.isPrepared

		if (isPrepared && song.mediaId.isNotEmpty()) transportControls.playFromMediaId(
			song.mediaId,
			null
		)
	}
}

@Immutable
data class AlbumScreenUiState(val albumsState: AlbumUiState)

sealed interface AlbumUiState {
	data class Success(val albums: List<Album>) : AlbumUiState
	object Error : AlbumUiState
	object Loading : AlbumUiState
}

@Immutable
data class SongListUiState(val songsState: SongUiState)

sealed interface SongUiState {
	data class Success(val albumId: Long, val songs: List<Song>) : SongUiState
	object Error : SongUiState
	object Loading : SongUiState
}