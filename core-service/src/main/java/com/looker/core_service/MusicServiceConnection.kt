package com.looker.core_service

import android.content.ComponentName
import android.content.Context
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaBrowserCompat.SubscriptionCallback
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.material.icons.rounded.PlayArrow
import com.looker.core_model.Song
import com.looker.core_service.utils.extension.id
import com.looker.core_service.utils.extension.isPlaying
import com.looker.core_service.utils.extension.toSong
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class MusicServiceConnection(context: Context) {

	private val _isConnected = MutableStateFlow(false)
	private val _shuffleMode = MutableStateFlow(false)
	private val _isPlaying = MutableStateFlow(false)
	private val _playIcon = MutableStateFlow(Icons.Rounded.PlayArrow)
	private val _playbackState = MutableStateFlow(EMPTY_PLAYBACK_STATE)
	private val _nowPlaying = MutableStateFlow(NOTHING_PLAYING)
	private val _currentQueue = MutableStateFlow<List<CustomQueueItem>>(emptyList())

	val shuffleMode = _shuffleMode.asStateFlow()
	val isPlaying = _isPlaying.asStateFlow()
	val playIcon = _playIcon.asStateFlow()
	val playbackState = _playbackState.asStateFlow()
	val nowPlaying = _nowPlaying.asStateFlow()
	val currentQueue = _currentQueue.asStateFlow()

	private var previousPlayState: Boolean = false

	lateinit var mediaController: MediaControllerCompat

	val transportControls: MediaControllerCompat.TransportControls
		get() = mediaController.transportControls

	private val mediaBrowserConnectionCallback = MediaBrowserConnectionCallback(context)

	private val mediaBrowser = MediaBrowserCompat(
		context,
		ComponentName(context, MusicService::class.java),
		mediaBrowserConnectionCallback,
		null
	).apply { connect() }

	fun subscribe(parentId: String, callback: (List<Song>) -> Unit = {}) {
		mediaBrowser.subscribe(
			parentId,
			object : SubscriptionCallback() {
				override fun onChildrenLoaded(
					parentId: String,
					children: MutableList<MediaBrowserCompat.MediaItem>
				) {
					super.onChildrenLoaded(parentId, children)
					callback(children.map { it.toSong })
				}
			}
		)
	}

	fun unsubscribe(
		parentId: String,
		callback: SubscriptionCallback = object : SubscriptionCallback() {}
	) {
		mediaBrowser.unsubscribe(parentId, callback)
	}

	private inner class MediaBrowserConnectionCallback(
		private val context: Context
	) : MediaBrowserCompat.ConnectionCallback() {
		override fun onConnected() {
			mediaController = MediaControllerCompat(context, mediaBrowser.sessionToken).apply {
				registerCallback(MediaControllerCallback())
			}
			_isConnected.value = true
		}

		override fun onConnectionSuspended() {
			_isConnected.value = false
		}

		override fun onConnectionFailed() {
			_isConnected.value = false
		}
	}

	private inner class MediaControllerCallback : MediaControllerCompat.Callback() {

		override fun onQueueChanged(queue: MutableList<MediaSessionCompat.QueueItem>?) {
			_currentQueue.value = queue?.map {
				CustomQueueItem(it.queueId, it.description.toSong)
			} ?: emptyList()
		}

		override fun onShuffleModeChanged(shuffleMode: Int) {
			super.onShuffleModeChanged(shuffleMode)
			_shuffleMode.value = shuffleMode == PlaybackStateCompat.SHUFFLE_MODE_ALL
					|| shuffleMode == PlaybackStateCompat.SHUFFLE_MODE_ALL
		}

		override fun onPlaybackStateChanged(state: PlaybackStateCompat?) {
			_playbackState.value = state ?: EMPTY_PLAYBACK_STATE
			_playIcon.value = if (state?.isPlaying == true) Icons.Rounded.Pause
			else Icons.Rounded.PlayArrow
			if (state?.isPlaying != previousPlayState) _isPlaying.value = state?.isPlaying == true
			previousPlayState = state?.isPlaying == true
		}

		override fun onMetadataChanged(metadata: MediaMetadataCompat?) {
			_nowPlaying.value = if (metadata?.id == null) NOTHING_PLAYING else metadata

		}

		override fun onSessionDestroyed() {
			mediaBrowserConnectionCallback.onConnectionSuspended()
		}
	}
}

data class CustomQueueItem(
	val queueId: Long,
	val song: Song
)

val EMPTY_PLAYBACK_STATE: PlaybackStateCompat = PlaybackStateCompat.Builder()
	.setState(PlaybackStateCompat.STATE_NONE, 0, 0f)
	.build()

val NOTHING_PLAYING: MediaMetadataCompat = MediaMetadataCompat.Builder()
	.putString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID, "")
	.putLong(MediaMetadataCompat.METADATA_KEY_DURATION, 0)
	.build()