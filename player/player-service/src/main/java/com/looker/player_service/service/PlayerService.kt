package com.looker.player_service.service

import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.media.session.MediaSession
import android.os.IBinder
import android.support.v4.media.session.MediaSessionCompat
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector
import com.looker.constants.Constants.NOTIFICATION_ID
import com.looker.domain_music.emptySong
import com.looker.player_service.notification.NotificationBuilder
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PlayerService : Service() {

    private val serviceContext = this
    @Inject
    lateinit var player: SimpleExoPlayer
    val currentSong = emptySong

    @Inject
    lateinit var mediaSession: MediaSession
    private lateinit var mediaSessionConnector: MediaSessionConnector
    private lateinit var notification: NotificationBuilder
    @Inject
    lateinit var notificationManager: NotificationManager

    override fun onCreate() {
        super.onCreate()

        mediaSessionConnector =
            MediaSessionConnector(
                MediaSessionCompat.fromMediaSession(serviceContext, mediaSession)
            )

        mediaSessionConnector.setPlayer(player)

        notification =
            NotificationBuilder.from(
                serviceContext,
                notificationManager,
                mediaSession,
                currentSong
            )

        startForeground(NOTIFICATION_ID, notification.build())
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int = START_NOT_STICKY
    override fun onBind(intent: Intent): IBinder? = null

    override fun onDestroy() {
        super.onDestroy()
        player.release()
        mediaSession.release()
    }
}
