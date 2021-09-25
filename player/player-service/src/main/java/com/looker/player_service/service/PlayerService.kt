package com.looker.player_service.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_DEFAULT
import android.content.Intent
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.session.MediaSessionCompat
import androidx.core.app.NotificationCompat
import androidx.media.MediaBrowserServiceCompat
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector
import com.looker.constants.Constants.NOTIFICATION_CHANNEL_ID
import com.looker.constants.Constants.NOTIFICATION_CHANNEL_NAME
import com.looker.constants.Constants.NOTIFICATION_ID
import com.looker.player_service.R
import androidx.media.app.NotificationCompat as MediaNotificationCompat

class PlayerService : MediaBrowserServiceCompat() {

    private lateinit var player: SimpleExoPlayer

    private lateinit var notificationManager: NotificationManager
    private lateinit var mediaSession: MediaSessionCompat

    override fun onCreate() {
        super.onCreate()

        player = SimpleExoPlayer.Builder(this).build()

        notificationManager =
            getSystemService(NotificationManager::class.java) as NotificationManager

        val channel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID, NOTIFICATION_CHANNEL_NAME, IMPORTANCE_DEFAULT
        )

        notificationManager.createNotificationChannel(channel)

        mediaSession = MediaSessionCompat(this, "Howl")
        mediaSession.apply {
            this@PlayerService.sessionToken = sessionToken
            isActive = true
        }

        val mediaSessionConnector = MediaSessionConnector(mediaSession)
        mediaSessionConnector.setPlayer(player)
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {

        showNotification()

        return START_NOT_STICKY
    }

    private fun showNotification() {

        val builder: NotificationCompat.Builder =
            NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)

        builder.apply {
            setStyle(
                MediaNotificationCompat.MediaStyle()
                    .setMediaSession(mediaSession.sessionToken)
            )
            setContentTitle("TEST NOTIFICATION")
            setContentText("Playing")
            setSmallIcon(R.drawable.ic_pause)
        }

        startForeground(NOTIFICATION_ID, builder.build())
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

    override fun onDestroy() {
        super.onDestroy()
        player.release()
        mediaSession.release()
    }
}