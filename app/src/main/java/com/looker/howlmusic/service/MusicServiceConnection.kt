package com.looker.howlmusic.service

import android.content.ComponentName
import android.content.Context
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaBrowserCompat.SubscriptionCallback
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.PlaybackStateCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.looker.howlmusic.utils.extension.id

class MusicServiceConnection(context: Context) {

	private val _isConnected = MutableLiveData<Boolean>()

	private val _playbackState = MutableLiveData<PlaybackStateCompat>()
	val playbackState: LiveData<PlaybackStateCompat> = _playbackState

	private val _nowPlaying = MutableLiveData<MediaMetadataCompat?>()
	val nowPlaying: LiveData<MediaMetadataCompat?> = _nowPlaying

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
			_isConnected.postValue(true)
		}

		override fun onConnectionSuspended() {
			_isConnected.postValue(false)
		}

		override fun onConnectionFailed() {
			_isConnected.postValue(false)
		}
	}

	private inner class MediaControllerCallback : MediaControllerCompat.Callback() {

		override fun onPlaybackStateChanged(state: PlaybackStateCompat?) {
			_playbackState.postValue(state ?: EMPTY_PLAYBACK_STATE)
		}

		override fun onMetadataChanged(metadata: MediaMetadataCompat?) {
			_nowPlaying.postValue(
				if (metadata?.id == null) NOTHING_PLAYING
				else metadata
			)
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