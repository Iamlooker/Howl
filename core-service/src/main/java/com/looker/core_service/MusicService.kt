package com.looker.core_service

import android.app.Notification
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.media.MediaBrowserServiceCompat
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.PlaybackException
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector
import com.google.android.exoplayer2.ext.mediasession.TimelineQueueNavigator
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import com.google.android.exoplayer2.upstream.DefaultDataSource
import com.google.android.exoplayer2.util.EventLogger
import com.looker.core_common.Constants
import com.looker.core_common.Constants.MEDIA_ROOT_ID
import com.looker.core_service.callback.MusicPlaybackPreparer
import com.looker.core_service.data.DataSource
import com.looker.core_service.notification.MusicNotificationManager
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

	private var isForegroundServiceOn = false

	private var isPlayerInitialized = false

	override fun onCreate() {
		super.onCreate()

		serviceScope.launch { musicSource.load() }

		val activityIntent = packageManager?.getLaunchIntentForPackage(packageName)?.let {
			PendingIntent.getActivity(
				this, 0, it,
				PendingIntent.FLAG_UPDATE_CURRENT or if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else 0
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
			notificationListener = MusicPlayerNotificationListener()
		)

		val musicPlaybackPreparer = MusicPlaybackPreparer(musicSource) {
			preparePlayer(
				itemToPlay = it,
				playNow = true
			)
		}

		mediaSessionConnector = MediaSessionConnector(mediaSession)
		mediaSessionConnector.setPlaybackPreparer(musicPlaybackPreparer)
		mediaSessionConnector.setQueueNavigator(MusicQueueNavigator())
		mediaSessionConnector.setPlayer(exoPlayer)

		musicPlayerEventListener = MusicPlayerEventListener()

		exoPlayer.addListener(musicPlayerEventListener)
		exoPlayer.addAnalyticsListener(EventLogger(null))
		musicNotificationManager.showNotification(exoPlayer)
	}

	private inner class MusicQueueNavigator : TimelineQueueNavigator(mediaSession) {
		override fun getMediaDescription(player: Player, windowIndex: Int): MediaDescriptionCompat =
			musicSource.data[windowIndex].description
	}

	private fun preparePlayer(
		songs: List<MediaMetadataCompat> = musicSource.data,
		itemToPlay: MediaMetadataCompat? = null,
		playNow: Boolean = false
	) {
		val currentSongIndex = if (itemToPlay == null) 0 else songs.indexOf(itemToPlay)
		exoPlayer.setMediaSource(musicSource.asMediaSource(dataSourceFactory))
		exoPlayer.prepare()
		exoPlayer.seekTo(currentSongIndex, 0L)
		exoPlayer.playWhenReady = playNow
	}

	override fun onGetRoot(
		clientPackageName: String,
		clientUid: Int,
		rootHints: Bundle?
	): BrowserRoot = BrowserRoot(MEDIA_ROOT_ID, null)

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
							preparePlayer()
							isPlayerInitialized = true
						}
					} else result.sendResult(null)
				}
				if (!resultsSent) result.detach()
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

	private inner class MusicPlayerNotificationListener :
		PlayerNotificationManager.NotificationListener {
		override fun onNotificationCancelled(notificationId: Int, dismissedByUser: Boolean) {
			stopForeground(true)
			isForegroundServiceOn = false
			stopSelf()
		}

		override fun onNotificationPosted(
			notificationId: Int,
			notification: Notification,
			ongoing: Boolean
		) {
			if (ongoing && !isForegroundServiceOn) {
				ContextCompat.startForegroundService(
					applicationContext,
					Intent(applicationContext, this@MusicService.javaClass)
				)
				startForeground(Constants.NOTIFICATION_ID, notification)
				isForegroundServiceOn = true
			}
		}
	}

	private inner class MusicPlayerEventListener : Player.Listener {

		override fun onPlaybackStateChanged(playbackState: Int) {
			when (playbackState) {
				Player.STATE_BUFFERING,
				Player.STATE_READY -> {
					musicNotificationManager.showNotification(exoPlayer)
					stopForeground(false)
					isForegroundServiceOn = false
				}
				else -> musicNotificationManager.hideNotification()
			}
		}

		override fun onPlayerError(error: PlaybackException) {
			Toast.makeText(applicationContext, "Error Occurred", Toast.LENGTH_SHORT).show()
		}
	}
}