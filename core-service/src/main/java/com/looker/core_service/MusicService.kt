package com.looker.core_service

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.ResultReceiver
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaBrowserCompat.MediaItem.FLAG_PLAYABLE
import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.media.MediaBrowserServiceCompat
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.PlaybackException
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.Player.EVENT_MEDIA_ITEM_TRANSITION
import com.google.android.exoplayer2.Player.EVENT_PLAY_WHEN_READY_CHANGED
import com.google.android.exoplayer2.Player.EVENT_POSITION_DISCONTINUITY
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector
import com.google.android.exoplayer2.ext.mediasession.TimelineQueueNavigator
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import com.google.android.exoplayer2.upstream.DefaultDataSource
import com.google.android.exoplayer2.util.EventLogger
import com.google.android.exoplayer2.util.Util.constrainValue
import com.looker.core_common.Constants
import com.looker.core_common.Constants.MEDIA_ROOT_ID
import com.looker.core_data.repository.BlacklistsRepository
import com.looker.core_service.extensions.toMediaItem
import com.looker.core_service.library.MusicSource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject

const val TAG = "MusicService"

@AndroidEntryPoint
class MusicService : MediaBrowserServiceCompat() {

	@Inject
	lateinit var exoPlayer: ExoPlayer

	@Inject
	lateinit var musicSource: MusicSource

	@Inject
	lateinit var dataSourceFactory: DefaultDataSource.Factory

	@Inject
	lateinit var blacklistsRepository: BlacklistsRepository

	@Inject
	lateinit var storage: PersistentStorage

	private val serviceJob = SupervisorJob()
	private val serviceScope = CoroutineScope(Dispatchers.Main + serviceJob)

	private lateinit var musicNotificationManager: MusicNotificationManager

	private lateinit var mediaSession: MediaSessionCompat
	private lateinit var mediaSessionConnector: MediaSessionConnector
	private lateinit var musicPlayerEventListener: MusicPlayerEventListener

	private var currentPlaylistItems: List<MediaMetadataCompat> = emptyList()
	private var currentMediaItemIndex: Int = 0

	private var isForegroundServiceOn = false

	override fun onCreate() {
		super.onCreate()

		val activityIntent = packageManager?.getLaunchIntentForPackage(packageName)?.let {
			PendingIntent.getActivity(
				this, 0, it,
				PendingIntent.FLAG_UPDATE_CURRENT or if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else 0
			)
		}

		mediaSession = MediaSessionCompat(this, TAG).apply {
			setSessionActivity(activityIntent)
			isActive = true
		}
		sessionToken = mediaSession.sessionToken

		musicNotificationManager = MusicNotificationManager(
			context = this,
			sessionToken = mediaSession.sessionToken,
			notificationListener = MusicPlayerNotificationListener()
		)

		serviceScope.launch { musicSource.load() }

		mediaSessionConnector = MediaSessionConnector(mediaSession)
		mediaSessionConnector.setPlaybackPreparer(MusicPlaybackPreparer())
		mediaSessionConnector.setQueueNavigator(MusicQueueNavigator())
		mediaSessionConnector.setPlayer(exoPlayer)

		musicPlayerEventListener = MusicPlayerEventListener()

		exoPlayer.addListener(musicPlayerEventListener)
		exoPlayer.addAnalyticsListener(EventLogger())
		musicNotificationManager.showNotification(exoPlayer)
	}

	override fun onTaskRemoved(rootIntent: Intent?) {
		saveRecentSongToStorage()
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

	override fun onGetRoot(
		clientPackageName: String,
		clientUid: Int,
		rootHints: Bundle?
	): BrowserRoot = BrowserRoot(MEDIA_ROOT_ID, null)

	override fun onLoadChildren(
		parentId: String,
		result: Result<MutableList<MediaBrowserCompat.MediaItem>>
	) {
		if (MEDIA_ROOT_ID == parentId) {
			val resultsSent = musicSource.whenReady { isInitialized ->
				if (isInitialized) {
					val children = musicSource.map {
						MediaBrowserCompat.MediaItem(it.description, FLAG_PLAYABLE)
					}.toMutableList()
					if (storage.loadRecentSong() == null)
						preparePlaylist(musicSource.toList())
					result.sendResult(children)
				} else result.sendResult(null)
			}
			if (!resultsSent) result.detach()
		}
	}

	private inner class MusicQueueNavigator : TimelineQueueNavigator(mediaSession) {
		override fun getMediaDescription(player: Player, windowIndex: Int): MediaDescriptionCompat =
			currentPlaylistItems[windowIndex].description

	}

	private inner class MusicPlayerNotificationListener :
		PlayerNotificationManager.NotificationListener {
		override fun onNotificationCancelled(notificationId: Int, dismissedByUser: Boolean) {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
				stopForeground(Service.STOP_FOREGROUND_REMOVE)
			} else stopForeground(true)
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

		override fun onEvents(player: Player, events: Player.Events) {
			if (events.contains(EVENT_POSITION_DISCONTINUITY)
				|| events.contains(EVENT_MEDIA_ITEM_TRANSITION)
				|| events.contains(EVENT_PLAY_WHEN_READY_CHANGED)
			) {
				currentMediaItemIndex = if (currentPlaylistItems.isNotEmpty()) {
					constrainValue(
						player.currentMediaItemIndex,
						0,
						currentPlaylistItems.size - 1
					)
				} else 0
			}
		}

		override fun onPlaybackStateChanged(playbackState: Int) {
			when (playbackState) {
				Player.STATE_BUFFERING,
				Player.STATE_READY -> {
					musicNotificationManager.showNotification(exoPlayer)
					if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
						stopForeground(Service.STOP_FOREGROUND_REMOVE)
					} else stopForeground(true)
					isForegroundServiceOn = false
				}
				else -> musicNotificationManager.hideNotification()
			}
		}

		override fun onPlayerError(error: PlaybackException) {
			Toast.makeText(applicationContext, "Error Occurred", Toast.LENGTH_SHORT).show()
		}
	}

	inner class MusicPlaybackPreparer :
		MediaSessionConnector.PlaybackPreparer {
		override fun onCommand(
			player: Player,
			command: String,
			extras: Bundle?,
			cb: ResultReceiver?
		): Boolean = false

		override fun getSupportedPrepareActions(): Long =
			PlaybackStateCompat.ACTION_PREPARE_FROM_MEDIA_ID or PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID

		override fun onPrepare(playWhenReady: Boolean) {
			val recentSong = storage.loadRecentSong() ?: return
			onPrepareFromMediaId(
				recentSong.mediaId!!,
				playWhenReady,
				recentSong.description.extras
			)
		}

		override fun onPrepareFromMediaId(
			mediaId: String,
			playWhenReady: Boolean,
			extras: Bundle?
		) {
			musicSource.whenReady {
				val itemToPlay = musicSource.find { mediaId == it.description.mediaId }
				if (itemToPlay == null) {
					Log.w(TAG, "Content not found: MediaID=$mediaId")
				} else {
					val playbackStartPositionMs =
						extras?.getLong(
							MEDIA_DESCRIPTION_EXTRAS_START_PLAYBACK_POSITION_MS,
							C.TIME_UNSET
						) ?: C.TIME_UNSET

					preparePlaylist(
						musicSource.toList(),
						itemToPlay,
						playWhenReady,
						playbackStartPositionMs
					)
				}
			}
		}

		override fun onPrepareFromSearch(query: String, playWhenReady: Boolean, extras: Bundle?) =
			Unit

		override fun onPrepareFromUri(uri: Uri, playWhenReady: Boolean, extras: Bundle?) = Unit
	}


	private fun preparePlaylist(
		metadataList: List<MediaMetadataCompat>,
		itemToPlay: MediaMetadataCompat? = null,
		playWhenReady: Boolean = false,
		playbackStartPositionMs: Long = 0L
	) {
		val initialWindowIndex = if (itemToPlay == null) 0 else metadataList.indexOf(itemToPlay)
		currentPlaylistItems = metadataList

		exoPlayer.playWhenReady = playWhenReady
		exoPlayer.stop()
		exoPlayer.setMediaItems(
			metadataList.map { it.toMediaItem() }, initialWindowIndex, playbackStartPositionMs
		)
		exoPlayer.prepare()
	}

	private fun saveRecentSongToStorage() {
		if (currentPlaylistItems.isEmpty()) return
		val description = currentPlaylistItems[currentMediaItemIndex].description
		val position = exoPlayer.currentPosition

		serviceScope.launch {
			storage.saveRecentSong(
				description,
				position
			)
		}
	}
}

const val MEDIA_DESCRIPTION_EXTRAS_START_PLAYBACK_POSITION_MS = "playback_start_position_ms"