package com.looker.player_service.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.support.v4.media.session.MediaSessionCompat
import androidx.core.app.NotificationCompat
import com.looker.constants.Constants.NOTIFICATION_CHANNEL_ID
import com.looker.constants.Constants.NOTIFICATION_CHANNEL_NAME
import com.looker.constants.Constants.NOTIFICATION_ID
import com.looker.player_service.R
import androidx.media.app.NotificationCompat as MediaAppNotificationCompat

class PlayerService : Service() {

    private lateinit var mediaSession: MediaSessionCompat
    private lateinit var notificationManager: NotificationManager
    private lateinit var notification: NotificationCompat.Builder

    override fun onCreate() {
        super.onCreate()

        notificationManager =
            getSystemService(NotificationManager::class.java) as NotificationManager

        val channel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID, NOTIFICATION_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_DEFAULT
        )

        notificationManager.createNotificationChannel(channel)

        mediaSession = MediaSessionCompat(this, "howlmusic")

        notification = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID).apply {
            setStyle(
                MediaAppNotificationCompat.MediaStyle()
                    .setMediaSession(mediaSession.sessionToken)
            )
            setSmallIcon(R.drawable.ic_play)
        }
        startForeground(NOTIFICATION_ID, notification.build())
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int = START_STICKY
    override fun onBind(intent: Intent): IBinder? = null

    override fun onDestroy() {
        super.onDestroy()
        mediaSession.release()
    }
}
