package com.looker.howlmusic.service

import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import androidx.media.MediaBrowserServiceCompat
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector
import com.google.android.exoplayer2.ext.mediasession.TimelineQueueNavigator
import com.google.android.exoplayer2.upstream.DefaultDataSource
import com.looker.constants.Constants.MEDIA_ROOT_ID
import com.looker.howlmusic.service.callback.MusicPlaybackPreparer
import com.looker.howlmusic.service.callback.MusicPlayerEventListener
import com.looker.howlmusic.service.callback.MusicPlayerNotificationListener
import com.looker.howlmusic.service.notification.MusicNotificationManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

const val SERVICE_TAG = "MusicService"

@AndroidEntryPoint
class MusicService : MediaBrowserServiceCompat() {

	@Inject
	lateinit var exoPlayer: ExoPlayer

	@Inject
	lateinit var musicSource: MusicSource

	private val serviceJob = Job()
	private val serviceScope = CoroutineScope(Dispatchers.Main + serviceJob)

	@Inject
	lateinit var dataSourceFactory: DefaultDataSource.Factory

	private lateinit var musicNotificationManager: MusicNotificationManager

	private lateinit var mediaSession: MediaSessionCompat
	private lateinit var mediaSessionConnector: MediaSessionConnector
	private lateinit var musicPlayerEventListener: MusicPlayerEventListener

	var isForegroundServiceOn = false

	private var currentSong: MediaMetadataCompat? = null

	private var isPlayerInitialized = false

	override fun onCreate() {
		super.onCreate()

		serviceScope.launch { musicSource.fetchMediaData() }

		val activityIntent = packageManager?.getLaunchIntentForPackage(packageName)?.let {
			PendingIntent.getActivity(
				this, 0, it,
				PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
			)
		}

		mediaSession = MediaSessionCompat(this, SERVICE_TAG).apply {
			setSessionActivity(activityIntent)
			isActive = true
		}
		sessionToken = mediaSession.sessionToken

		musicNotificationManager = MusicNotificationManager(
			context = this,
			sessionToken = mediaSession.sessionToken,
			notificationListener = MusicPlayerNotificationListener(this)
		) {
			currentSongDuration = exoPlayer.duration
		}

		val musicPlaybackPreparer = MusicPlaybackPreparer(musicSource) {
			currentSong = it
			preparePlayer(
				songs = musicSource.songs,
				itemToPlay = it,
				playNow = true
			)
		}

		mediaSessionConnector = MediaSessionConnector(mediaSession)
		mediaSessionConnector.setPlaybackPreparer(musicPlaybackPreparer)
		mediaSessionConnector.setQueueNavigator(MusicQueueNavigator())
		mediaSessionConnector.setPlayer(exoPlayer)

		musicPlayerEventListener = MusicPlayerEventListener(this)

		exoPlayer.addListener(musicPlayerEventListener)
		musicNotificationManager.showNotification(exoPlayer)
	}

	private inner class MusicQueueNavigator : TimelineQueueNavigator(mediaSession) {
		override fun getMediaDescription(player: Player, windowIndex: Int): MediaDescriptionCompat {
			return musicSource.songs[windowIndex].description
		}
	}

	private fun preparePlayer(
		songs: List<MediaMetadataCompat>,
		itemToPlay: MediaMetadataCompat?,
		playNow: Boolean
	) {
		val currentSongIndex = if (currentSong == null) 0 else songs.indexOf(itemToPlay)
		exoPlayer.setMediaSource(musicSource.asMediaSource(dataSourceFactory))
		exoPlayer.seekTo(currentSongIndex, 0L)
		exoPlayer.playWhenReady = playNow
	}

	override fun onGetRoot(
		clientPackageName: String,
		clientUid: Int,
		rootHints: Bundle?
	): BrowserRoot {
		return BrowserRoot(MEDIA_ROOT_ID, null)
	}

	override fun onLoadChildren(
		parentId: String,
		result: Result<MutableList<MediaBrowserCompat.MediaItem>>
	) {
		when (parentId) {
			MEDIA_ROOT_ID -> {
				val resultsSent = musicSource.whenReady { isInitialized ->
					if (isInitialized) {
						result.sendResult(musicSource.asMediaItem())
						if (!isPlayerInitialized && musicSource.songs.isNotEmpty()) {
							preparePlayer(musicSource.songs, musicSource.songs[0], false)
							isPlayerInitialized = true
						}
					} else {
						result.sendResult(null)
					}
				}
				if (!resultsSent) {
					result.detach()
				}
			}
		}
	}

	override fun onTaskRemoved(rootIntent: Intent?) {
		super.onTaskRemoved(rootIntent)
		exoPlayer.stop()
	}

	override fun onDestroy() {
		super.onDestroy()
		serviceJob.cancel()
		exoPlayer.removeListener(musicPlayerEventListener)
		exoPlayer.release()
	}

	companion object {
		var currentSongDuration = 0L
			private set
	}
}