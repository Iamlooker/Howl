package com.looker.howlmusic

import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaBrowserCompat.SubscriptionCallback
import android.util.Log
import androidx.compose.material.BackdropValue
import androidx.compose.material.BackdropValue.Concealed
import androidx.compose.material.BackdropValue.Revealed
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Shuffle
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.looker.components.state.PlayState
import com.looker.components.state.PlayState.PAUSED
import com.looker.components.state.PlayState.PLAYING
import com.looker.components.state.SheetsState
import com.looker.components.state.SheetsState.HIDDEN
import com.looker.components.state.SheetsState.VISIBLE
import com.looker.constants.Constants.MEDIA_ROOT_ID
import com.looker.constants.Resource
import com.looker.data_music.data.AlbumsRepository
import com.looker.domain_music.Album
import com.looker.domain_music.Song
import com.looker.domain_music.emptyAlbum
import com.looker.domain_music.emptySong
import com.looker.howlmusic.service.MusicService
import com.looker.howlmusic.service.MusicServiceConnection
import com.looker.howlmusic.utils.extension.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HowlViewModel
@Inject constructor(
	private val musicServiceConnection: MusicServiceConnection,
	private val albumsRepository: AlbumsRepository
) : ViewModel() {

	private val _mediaItems =
		MutableStateFlow<Resource<List<Song>>>(Resource.Loading(listOf(emptySong)))
	val mediaItems: StateFlow<Resource<List<Song>>> = _mediaItems

	private val _albumsList = MutableStateFlow(emptyList<Album>())
	val albumsList: StateFlow<List<Album>> = _albumsList

	init {
		musicServiceConnection.subscribe(
			MEDIA_ROOT_ID,
			object : SubscriptionCallback() {
				override fun onChildrenLoaded(
					parentId: String,
					children: MutableList<MediaBrowserCompat.MediaItem>
				) {
					super.onChildrenLoaded(parentId, children)
					val items = children.map { it.toSong }
					viewModelScope.launch { _mediaItems.emit(Resource.Success(items)) }
				}
			}
		)
		viewModelScope.launch(Dispatchers.IO) {
			albumsRepository.getAllAlbums().collect { _albumsList.emit(it) }
		}
	}

	val isConnected = musicServiceConnection.isConnected

	val nowPlaying = musicServiceConnection.nowPlaying

	private val _backdropValue = MutableStateFlow<SheetsState>(HIDDEN)
	private val _currentAlbum = MutableStateFlow(emptyAlbum)
	private val _currentSong = MutableStateFlow(emptySong)
	private val _enableGesture = MutableStateFlow(true)
	private val _playIcon = MutableStateFlow(Icons.Rounded.PlayArrow)
	private val _playState = MutableStateFlow<PlayState>(PAUSED)
	private val _progress = MutableStateFlow(0f)
	private val _toggle = MutableStateFlow(false)
	private val _toggleIcon = MutableStateFlow(Icons.Rounded.Shuffle)
	private val _currentSongDuration = MutableStateFlow(0L)

	val backdropValue: StateFlow<SheetsState> = _backdropValue
	val currentAlbum: StateFlow<Album> = _currentAlbum
	val currentSong: StateFlow<Song> = _currentSong
	val enableGesture: StateFlow<Boolean> = _enableGesture
	val progress: StateFlow<Float> = _progress
	val playState: StateFlow<PlayState> = _playState
	val toggle: StateFlow<Boolean> = _toggle
	val toggleIcon: StateFlow<ImageVector> = _toggleIcon
	private val currentSongDuration: StateFlow<Long> = _currentSongDuration

	@ExperimentalMaterialApi
	fun setBackdropValue(currentValue: BackdropValue) {
		viewModelScope.launch(Dispatchers.IO) {
			_backdropValue.emit(
				when (currentValue) {
					Concealed -> HIDDEN
					Revealed -> VISIBLE
				}
			)
		}
	}

	fun updateToggle() {
		viewModelScope.launch(Dispatchers.IO) {
			_toggle.emit(
				when (backdropValue.value) {
					HIDDEN -> when (playState.value) {
						PAUSED -> false
						PLAYING -> true
					}
					VISIBLE -> false
				}
			)
		}
	}

	fun gestureState(allowGesture: Boolean) {
		viewModelScope.launch { _enableGesture.emit(allowGesture) }
	}

	private suspend fun setPlayState(isPlaying: PlayState) {
		_playState.emit(isPlaying)
	}

	private suspend fun setPlayState(isPlaying: Boolean) {
		_playState.emit(if (isPlaying) PLAYING else PAUSED)
	}

	private suspend fun updatePlayIcon() {
		_playIcon.emit(
			when (playState.value) {
				is PAUSED -> Icons.Rounded.PlayArrow
				is PLAYING -> Icons.Rounded.Pause
			}
		)
	}

	private fun onPlayPause(isPlaying: PlayState) {
		viewModelScope.launch(Dispatchers.IO) { setPlayState(isPlaying) }
		viewModelScope.launch(Dispatchers.Main) {
			playMedia(currentSong.value)
			updatePlayIcon()
		}
	}

	fun onToggle(currentState: SheetsState, playState: PlayState) = when (currentState) {
		is HIDDEN -> onPlayPause(playState)
		is VISIBLE -> Unit
	}

	fun setToggleIcon(currentState: SheetsState) {
		viewModelScope.launch(Dispatchers.IO) {
			_playIcon.collect {
				_toggleIcon.emit(
					when (currentState) {
						is HIDDEN -> it
						is VISIBLE -> Icons.Rounded.Shuffle
					}
				)
			}
		}
	}

	fun onAlbumClick(index: Int) {
		viewModelScope.launch(Dispatchers.IO) {
			albumsList.collect { albums -> _currentAlbum.emit(albums[index]) }
		}
	}

	fun onSongClick(song: Song) {
		playMedia(song, pauseAllowed = false)
	}

	fun playMedia(mediaItem: Song, pauseAllowed: Boolean = true) {
		val nowPlaying = musicServiceConnection.nowPlaying.value
		val transportControls = musicServiceConnection.transportControls

		val isPrepared = musicServiceConnection.playbackState.value?.isPrepared ?: false
		if (isPrepared && mediaItem.mediaId == nowPlaying?.id) {
			musicServiceConnection.playbackState.value?.let { playbackState ->
				when {
					playbackState.isPlaying ->
						if (pauseAllowed) transportControls.pause() else Unit
					playbackState.isPlayEnabled -> transportControls.play()
					else -> {
						Log.w(
							"HowlViewModel",
							"Playable item clicked but neither play nor pause are enabled!" +
									" (mediaId=${mediaItem.mediaId})"
						)
					}
				}
			}
		} else {
			transportControls.playFromMediaId(mediaItem.mediaId, null)
		}
		musicServiceConnection.playbackState.value?.let {
			viewModelScope.launch { setPlayState(!it.isPlaying) }
		}
	}

	fun onSeek(seekTo: Float) {
		musicServiceConnection.transportControls.seekTo(
			currentSongDuration.value.times(seekTo).toLong()
		)
	}

	private fun updateCurrentPlayerPosition() {
		val playbackState = musicServiceConnection.playbackState
		viewModelScope.launch {
			while (true) {
				val pos =
					(playbackState.value?.currentPlayBackPosition?.div(MusicService.currentSongDuration))?.toFloat()
				if (progress.value != pos) {
					_progress.emit(pos ?: 0F)
					_currentSongDuration.emit(MusicService.currentSongDuration)
				}
			}
		}
	}

	fun playNext() {
		musicServiceConnection.transportControls.skipToNext()
	}

	fun playPrevious() {
		musicServiceConnection.transportControls.skipToPrevious()
	}

	override fun onCleared() {
		super.onCleared()
		musicServiceConnection.unsubscribe(MEDIA_ROOT_ID)
	}
}
