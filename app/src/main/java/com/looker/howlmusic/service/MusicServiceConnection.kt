package com.looker.howlmusic.service

import android.content.ComponentName
import android.content.Context
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.PlaybackStateCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.looker.constants.Event
import com.looker.constants.Resource

class MusicServiceConnection(context: Context) {

	private val _isConnected = MutableLiveData<Event<Resource<Boolean>>>()
	val isConnected: LiveData<Event<Resource<Boolean>>> = _isConnected

	private val _playbackState = MutableLiveData<PlaybackStateCompat?>()
	val playbackState: LiveData<PlaybackStateCompat?> = _playbackState

	private val _currentSong = MutableLiveData<MediaMetadataCompat?>()
	val currentSong: LiveData<MediaMetadataCompat?> = _currentSong

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

	fun subscribe(parentId: String, callback: MediaBrowserCompat.SubscriptionCallback) {
		mediaBrowser.subscribe(parentId, callback)
	}

	fun unsubscribe(parentId: String, callback: MediaBrowserCompat.SubscriptionCallback) {
		mediaBrowser.unsubscribe(parentId, callback)
	}

	private inner class MediaBrowserConnectionCallback(
		private val context: Context
	) : MediaBrowserCompat.ConnectionCallback() {
		override fun onConnected() {
			mediaController = MediaControllerCompat(context, mediaBrowser.sessionToken).apply {
				registerCallback(MediaControllerCallback())
			}
			_isConnected.postValue(Event(Resource.Success(true)))
		}

		override fun onConnectionSuspended() {
			_isConnected.postValue(
				Event(
					Resource.Error(
						"Connection with Service Suspended",
						false
					)
				)
			)
		}

		override fun onConnectionFailed() {
			_isConnected.postValue(
				Event(
					Resource.Error(
						"Connection Failed with Media Browser",
						false
					)
				)
			)
		}
	}

	private inner class MediaControllerCallback : MediaControllerCompat.Callback() {

		override fun onPlaybackStateChanged(state: PlaybackStateCompat?) {
			_playbackState.postValue(state)
		}

		override fun onMetadataChanged(metadata: MediaMetadataCompat?) {
			_currentSong.postValue(metadata)
		}

		override fun onSessionDestroyed() {
			mediaBrowserConnectionCallback.onConnectionSuspended()
		}
	}
}