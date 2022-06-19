package com.looker.feature_album

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.looker.core_common.combineAndStateIn
import com.looker.core_common.mapAndStateIn
import com.looker.core_common.result.Result
import com.looker.core_common.result.asResult
import com.looker.core_data.repository.AlbumsRepository
import com.looker.core_data.repository.BlacklistsRepository
import com.looker.core_model.Album
import com.looker.core_model.Blacklist
import com.looker.core_model.Song
import com.looker.core_service.MusicServiceConnection
import com.looker.core_service.utils.extension.playPauseMedia
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlbumsViewModel @Inject constructor(
	private val musicServiceConnection: MusicServiceConnection,
	private val albumsRepository: AlbumsRepository,
	private val blacklistsRepository: BlacklistsRepository
) : ViewModel() {

	init {
		viewModelScope.launch { albumsRepository.syncData() }
	}

	private val _currentAlbum = MutableStateFlow(Album())
	val currentAlbum = _currentAlbum.asStateFlow()

	private val blacklistStream =
		blacklistsRepository.getBlacklistSongs()

	private val albumsStream: Flow<Result<List<Album>>> =
		albumsRepository.getAlbumsStream().asResult()

	private val songsStream: Flow<Result<List<Song>>> =
		albumsRepository.getAllSongs().asResult()

	val albumsState = albumsStream.mapAndStateIn(
		scope = viewModelScope,
		initialValue = AlbumScreenUiState(AlbumUiState.Loading)
	) { albumsResult ->
		val albums = when (albumsResult) {
			Result.Loading -> AlbumUiState.Loading
			is Result.Error -> AlbumUiState.Error
			is Result.Success -> AlbumUiState.Success(albumsResult.data)
		}
		AlbumScreenUiState(albums)
	}

	val songsState = combineAndStateIn(
		currentAlbum,
		songsStream,
		blacklistStream,
		scope = viewModelScope,
		initialValue = SongListUiState(SongUiState.Loading)
	) { currentAlbum, songsResult, blacklist ->
		val blacklistSongsFromAlbum = blacklist.flatMap(Blacklist::songsFromAlbum)
		val songs = when (songsResult) {
			Result.Loading -> SongUiState.Loading
			is Result.Error -> SongUiState.Error
			is Result.Success -> SongUiState.Success(
				currentAlbum.albumId.toString() in blacklistSongsFromAlbum,
				songsResult.data.filter { it.albumId == currentAlbum.albumId }
			)
		}
		SongListUiState(songs)
	}

	fun setCurrentAlbum(newAlbum: Album) {
		viewModelScope.launch {
			_currentAlbum.emit(newAlbum)
		}
	}

	fun playSong(song: Song) {
		song.playPauseMedia(
			musicServiceConnection = musicServiceConnection,
			canPause = false
		)
	}

	fun blacklistSongs(album: Album, add: Boolean = true) {
		viewModelScope.launch {
			if (add) blacklistsRepository.addToBlacklist(Blacklist(songsFromAlbum = setOf(album.albumId.toString())))
			else blacklistsRepository.allowSongsFromAlbum(setOf(album.albumId.toString()))
		}
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
	data class Success(val songsAreBlacklisted: Boolean, val songs: List<Song>) : SongUiState
	object Error : SongUiState
	object Loading : SongUiState
}