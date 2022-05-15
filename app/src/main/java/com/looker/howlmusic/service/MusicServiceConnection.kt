package com.looker.howlmusic.service

import android.content.ComponentName
import android.content.Context
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaBrowserCompat.SubscriptionCallback
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.PlaybackStateCompat
import com.looker.howlmusic.utils.extension.id
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class MusicServiceConnection(context: Context) {

	private val _isConnected = MutableStateFlow(false)

	private val _playbackState = MutableStateFlow(EMPTY_PLAYBACK_STATE)
	val playbackState = _playbackState.asStateFlow()

	private val _nowPlaying = MutableStateFlow(NOTHING_PLAYING)
	val nowPlaying = _nowPlaying.asStateFlow()

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

	fun subscribe(parentId: String, callback: SubscriptionCallback) {
		mediaBrowser.subscribe(parentId, callback)
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

		override fun onPlaybackStateChanged(state: PlaybackStateCompat?) {
			_playbackState.value = state ?: EMPTY_PLAYBACK_STATE
		}

		override fun onMetadataChanged(metadata: MediaMetadataCompat?) {
			_nowPlaying.value = if (metadata?.id == null) NOTHING_PLAYING else metadata

		}

		override fun onSessionDestroyed() {
			mediaBrowserConnectionCallback.onConnectionSuspended()
		}
	}
}

val EMPTY_PLAYBACK_STATE: PlaybackStateCompat = PlaybackStateCompat.Builder()
	.setState(PlaybackStateCompat.STATE_NONE, 0, 0f)
	.build()

val NOTHING_PLAYING: MediaMetadataCompat = MediaMetadataCompat.Builder()
	.putString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID, "")
	.putLong(MediaMetadataCompat.METADATA_KEY_DURATION, 0)
	.build()