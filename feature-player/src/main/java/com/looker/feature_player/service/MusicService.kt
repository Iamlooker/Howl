package com.looker.feature_player.service

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
import com.google.android.exoplayer2.util.EventLogger
import com.looker.constants.Constants.MEDIA_ROOT_ID
import com.looker.feature_player.service.callback.MusicPlaybackPreparer
import com.looker.feature_player.service.callback.MusicPlayerEventListener
import com.looker.feature_player.service.callback.MusicPlayerNotificationListener
import com.looker.feature_player.service.notification.MusicNotificationManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject

const val SERVICE_TAG = "MusicService"

@AndroidEntryPoint
class MusicService : MediaBrowserServiceCompat() {

	@Inject
	lateinit var exoPlayer: ExoPlayer

	@Inject
	lateinit var musicSource: DataSource<MediaMetadataCompat>

	private val serviceJob = SupervisorJob()
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

		serviceScope.launch { musicSource.load() }

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
			songDuration = exoPlayer.duration
		}

		val musicPlaybackPreparer = MusicPlaybackPreparer(musicSource) {
			currentSong = it
			preparePlayer(
				songs = musicSource.data,
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
		exoPlayer.addAnalyticsListener(EventLogger(null))
		musicNotificationManager.showNotification(exoPlayer)
	}

	private inner class MusicQueueNavigator : TimelineQueueNavigator(mediaSession) {
		override fun getMediaDescription(player: Player, windowIndex: Int): MediaDescriptionCompat {
			return musicSource.data[windowIndex].description
		}
	}

	private fun preparePlayer(
		songs: List<MediaMetadataCompat>,
		itemToPlay: MediaMetadataCompat?,
		playNow: Boolean
	) {
		val currentSongIndex = if (currentSong == null) 0 else songs.indexOf(itemToPlay)
		exoPlayer.setMediaSource(musicSource.asMediaSource(dataSourceFactory))
		exoPlayer.prepare()
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
				val resultsSent = musicSource.sourceReady { isInitialized ->
					if (isInitialized) {
						result.sendResult(musicSource.asMediaItem())
						if (!isPlayerInitialized && musicSource.data.isNotEmpty()) {
							preparePlayer(musicSource.data, musicSource.data[0], false)
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
		mediaSession.run {
			isActive = false
			release()
		}
		serviceJob.cancel()
		exoPlayer.removeListener(musicPlayerEventListener)
		exoPlayer.release()
	}

	companion object {
		var songDuration = 0L
			private set
	}
}