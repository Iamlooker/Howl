package com.looker.howlmusic

import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaBrowserCompat.SubscriptionCallback
import android.support.v4.media.MediaMetadataCompat.METADATA_KEY_MEDIA_ID
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
import com.looker.howlmusic.service.MusicServiceConnection
import com.looker.howlmusic.utils.extension.isPlayEnabled
import com.looker.howlmusic.utils.extension.isPlaying
import com.looker.howlmusic.utils.extension.isPrepared
import com.looker.howlmusic.utils.extension.toSong
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

	private val _mediaItems = MutableStateFlow<Resource<List<Song>>>(
		Resource.Loading(listOf(emptySong))
	)
	val mediaItems: StateFlow<Resource<List<Song>>> = _mediaItems

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
	val currentPlayingSong = musicServiceConnection.currentSong
	private val playbackState = musicServiceConnection.playbackState

	private val _albumsList = MutableStateFlow(emptyList<Album>())
	val albumsList: StateFlow<List<Album>> = _albumsList

	private val _backdropValue = MutableStateFlow<SheetsState>(HIDDEN)
	private val _currentAlbum = MutableStateFlow(emptyAlbum)
	private val _currentSong = MutableStateFlow(emptySong)
	private val _enableGesture = MutableStateFlow(true)
	private val _playIcon = MutableStateFlow(Icons.Rounded.PlayArrow)
	private val _playState = MutableStateFlow<PlayState>(PAUSED)
	private val _progress = MutableStateFlow(0f)
	private val _toggle = MutableStateFlow(false)
	private val _toggleIcon = MutableStateFlow(Icons.Rounded.Shuffle)

	val backdropValue: StateFlow<SheetsState> = _backdropValue
	val currentAlbum: StateFlow<Album> = _currentAlbum
	val currentSong: StateFlow<Song> = _currentSong
	val enableGesture: StateFlow<Boolean> = _enableGesture
	val progress: StateFlow<Float> = _progress
	val playState: StateFlow<PlayState> = _playState
	val toggle: StateFlow<Boolean> = _toggle
	val toggleIcon: StateFlow<ImageVector> = _toggleIcon

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
			// TODO: Set Play State
//			setPlayState(exoPlayer.isPlaying)
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

	fun playOrToggleSong(mediaItem: Song, toggle: Boolean = false) {
		val isPrepared = playbackState.value?.isPrepared ?: false
		if (isPrepared && mediaItem.mediaId
			== currentPlayingSong.value?.getString(METADATA_KEY_MEDIA_ID)
		) {
			playbackState.value?.let { playbackState ->
				when {
					playbackState.isPlaying -> if (toggle) musicServiceConnection.transportControls.pause()
					playbackState.isPlayEnabled -> musicServiceConnection.transportControls.play()
					else -> Unit
				}
			}
		} else {
			musicServiceConnection.transportControls.playFromMediaId(mediaItem.mediaId, null)
		}
	}

	fun onSeek(seekTo: Float) {
//		musicServiceConnection.transportControls.seekTo((exoPlayer.contentDuration * seekTo).toLong())
	}

	fun playNext() {
		musicServiceConnection.transportControls.skipToNext()
	}

	fun playPrevious() {
		musicServiceConnection.transportControls.skipToPrevious()
	}

	override fun onCleared() {
		super.onCleared()
		musicServiceConnection.unsubscribe(MEDIA_ROOT_ID, object : SubscriptionCallback() {})
	}
}
