package com.looker.player_service.service

import android.app.Notification
import android.content.Intent
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.session.MediaSessionCompat
import androidx.core.content.ContextCompat
import androidx.media.MediaBrowserServiceCompat
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector
import com.google.android.exoplayer2.ui.PlayerNotificationManager

class PlayerService : MediaBrowserServiceCompat() {

    private lateinit var player: SimpleExoPlayer

    private lateinit var notificationManager: HowlNotificationManager

    private lateinit var mediaSession: MediaSessionCompat

    private var isForegroundService = false

    override fun onCreate() {
        super.onCreate()

        player = SimpleExoPlayer.Builder(this).build()

        mediaSession = MediaSessionCompat(this, "Howl")
        mediaSession.apply {
            this@PlayerService.sessionToken = sessionToken
            isActive = true
        }
        notificationManager = HowlNotificationManager(
            this,
            mediaSession.sessionToken,
            PlayerNotificationListener()
        )

        notificationManager.showNotificationForPlayer(player)

        val mediaSessionConnector = MediaSessionConnector(mediaSession)
        mediaSessionConnector.setPlayer(player)
    }

    override fun onGetRoot(
        clientPackageName: String,
        clientUid: Int,
        rootHints: Bundle?
    ): BrowserRoot {
        return BrowserRoot(
            "Howl",
            null
        )
    }

    override fun onLoadChildren(
        parentId: String,
        result: Result<MutableList<MediaBrowserCompat.MediaItem>>
    ) {
        TODO("Not yet implemented")
    }

    private inner class PlayerNotificationListener :
        PlayerNotificationManager.NotificationListener {
        override fun onNotificationPosted(
            notificationId: Int,
            notification: Notification,
            ongoing: Boolean
        ) {
            if (ongoing && !isForegroundService) {
                ContextCompat.startForegroundService(
                    applicationContext,
                    Intent(applicationContext, this@PlayerService::class.java)
                )

                startForeground(notificationId, notification)
                isForegroundService = true
            }
        }

        override fun onNotificationCancelled(notificationId: Int, dismissedByUser: Boolean) {
            stopForeground(true)
            isForegroundService = false
            stopSelf()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        player.release()
        mediaSession.release()
    }
}