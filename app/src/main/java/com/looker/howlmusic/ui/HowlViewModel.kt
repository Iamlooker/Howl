package com.looker.howlmusic.ui

import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaBrowserCompat.SubscriptionCallback
import android.support.v4.media.session.PlaybackStateCompat.SHUFFLE_MODE_ALL
import android.support.v4.media.session.PlaybackStateCompat.SHUFFLE_MODE_NONE
import android.util.Log
import androidx.compose.material.BackdropValue
import androidx.compose.material.BackdropValue.Concealed
import androidx.compose.material.BackdropValue.Revealed
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Shuffle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.looker.components.state.SheetsState
import com.looker.components.state.SheetsState.HIDDEN
import com.looker.components.state.SheetsState.VISIBLE
import com.looker.constants.Constants.MEDIA_ROOT_ID
import com.looker.constants.Resource
import com.looker.constants.states.ToggleState
import com.looker.core_model.Album
import com.looker.core_model.Song
import com.looker.data_music.data.AlbumsRepository
import com.looker.howlmusic.service.EMPTY_PLAYBACK_STATE
import com.looker.howlmusic.service.MusicService
import com.looker.howlmusic.service.MusicServiceConnection
import com.looker.howlmusic.utils.extension.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

typealias ResourceSongs = Resource<List<Song>>

@HiltViewModel
class HowlViewModel
@Inject constructor(
	private val musicServiceConnection: MusicServiceConnection,
	private val albumsRepository: AlbumsRepository
) : ViewModel() {

	val nowPlaying = musicServiceConnection.nowPlaying

	val playbackState = musicServiceConnection.playbackState.stateIn(
		scope = viewModelScope,
		started = SharingStarted.WhileSubscribed(5000),
		initialValue = EMPTY_PLAYBACK_STATE
	)

	private val _songDuration = MutableStateFlow(0L)

	private val _backdropValue = MutableStateFlow<SheetsState>(HIDDEN)
	private val _currentAlbum = MutableStateFlow(Album())
	private val _enableGesture = MutableStateFlow(true)
	private val _playIcon = MutableStateFlow(Icons.Rounded.PlayArrow)
	private val _toggle = MutableStateFlow<ToggleState>(ToggleState.Shuffle)
	private val _toggleIcon = MutableStateFlow(Icons.Rounded.Shuffle)
	private val _progress = MutableStateFlow(0F)
	private val _albumsList = MutableStateFlow(emptyList<Album>())
	private val _songsList = MutableStateFlow<ResourceSongs>(Resource.Loading(listOf()))
	private val _shuffleMode = MutableStateFlow(0)

	val backdropValue = _backdropValue.asStateFlow()
	val currentAlbum = _currentAlbum.asStateFlow()
	val enableGesture = _enableGesture.asStateFlow()
	val playIcon = _playIcon.asStateFlow()
	val toggle = _toggle.asStateFlow()
	val toggleIcon = _toggleIcon.asStateFlow()
	val progress = _progress.asStateFlow()
	val albumsList = _albumsList.asStateFlow()
	val songsList = _songsList.asStateFlow()
	private val shuffleMode = _shuffleMode.asStateFlow()

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
					viewModelScope.launch {
						_songsList.emit(Resource.Success(items))
					}
				}
			}
		)
		viewModelScope.launch(Dispatchers.IO) {
			albumsRepository.getAllAlbums().collect { _albumsList.emit(it) }
		}

		updateCurrentPlayerPosition()
	}

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
					HIDDEN -> ToggleState.PlayControl
					VISIBLE -> ToggleState.Shuffle
				}
			)
		}
	}

	fun gestureState(allowGesture: Boolean) {
		viewModelScope.launch { _enableGesture.emit(allowGesture) }
	}

	fun setToggleIcon(isPlaying: Boolean) {
		viewModelScope.launch(Dispatchers.IO) {
			_playIcon.emit(
				if (isPlaying) Icons.Rounded.Pause else Icons.Rounded.PlayArrow
			)
			_toggleIcon.emit(
				when (backdropValue.value) {
					is HIDDEN -> if (isPlaying) Icons.Rounded.Pause else Icons.Rounded.PlayArrow
					is VISIBLE -> Icons.Rounded.Shuffle
				}
			)
		}
	}

	fun onToggleClick() {
		when (toggle.value) {
			ToggleState.PlayControl -> musicServiceConnection.transportControls.pause()
			ToggleState.Shuffle -> shuffleModeToggle()
		}
	}

	fun onAlbumClick(album: Album) {
		viewModelScope.launch(Dispatchers.IO) {
			albumsList.collect { _currentAlbum.emit(album) }
		}
	}

	fun onSongClick(song: Song) {
		playMedia(song, false)
	}

	fun playMedia(mediaItem: Song, pauseAllowed: Boolean = true) {
		val nowPlaying = musicServiceConnection.nowPlaying.value
		val transportControls = musicServiceConnection.transportControls

		val isPrepared = musicServiceConnection.playbackState.value.isPrepared
		musicServiceConnection.playbackState.value.let { playbackState ->
			if (isPrepared && mediaItem.mediaId == nowPlaying.id) {
				when {
					playbackState.isPlaying -> if (pauseAllowed) transportControls.pause() else Unit
					playbackState.isPlayEnabled -> transportControls.play()
					else -> {
						Log.w(
							"HowlViewModel",
							"Playable item clicked but neither play nor pause are enabled!" +
									" (mediaId=${mediaItem.mediaId})"
						)
					}
				}
			} else {
				transportControls.playFromMediaId(mediaItem.mediaId, null)
			}
		}
	}

	fun onSeek(seekTo: Float) {
		_progress.value = seekTo
		musicServiceConnection.transportControls.seekTo(
			_songDuration.value.times(seekTo).toLong()
		)
	}

	fun playNext() {
		musicServiceConnection.transportControls.skipToNext()
	}

	fun playPrevious() {
		musicServiceConnection.transportControls.skipToPrevious()
	}

	// TODO: Improve this
	private fun shuffleModeToggle() {
		val transportControls = musicServiceConnection.transportControls
		transportControls.setShuffleMode(if (shuffleMode.value == SHUFFLE_MODE_NONE) SHUFFLE_MODE_ALL else SHUFFLE_MODE_NONE)
		viewModelScope.launch {
			val toggleEnable = ToggleState.Shuffle
			toggleEnable.enabled = shuffleMode.value == SHUFFLE_MODE_ALL
			_toggle.emit(toggleEnable)
			_shuffleMode.emit(if (shuffleMode.value == SHUFFLE_MODE_NONE) SHUFFLE_MODE_ALL else SHUFFLE_MODE_NONE)
		}
	}

	private fun updateCurrentPlayerPosition() {
		viewModelScope.launch(Dispatchers.IO) {
			while (true) {
				val pos = playbackState.value.currentPlaybackPosition.toFloat()
				if (progress.value != pos) {
					_progress.emit(pos / MusicService.songDuration)
					_songDuration.emit(MusicService.songDuration)
				}
				delay(100)
			}
		}
	}

	override fun onCleared() {
		super.onCleared()
		musicServiceConnection.unsubscribe(MEDIA_ROOT_ID)
	}
}