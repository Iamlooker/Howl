package com.looker.player_service.service

import android.app.Notification
import android.app.Service
import android.content.Intent
import android.media.session.MediaSession
import android.os.IBinder
import com.google.android.exoplayer2.SimpleExoPlayer
import com.looker.constants.Constants.NOTIFICATION_CHANNEL_ID
import com.looker.constants.Constants.NOTIFICATION_ID
import com.looker.player_service.R
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PlayerService : Service() {

    @Inject
    lateinit var player: SimpleExoPlayer

    @Inject
    lateinit var mediaSession: MediaSession

    override fun onCreate() {
        super.onCreate()

        val mediaStyle: Notification.MediaStyle =
            Notification.MediaStyle().setMediaSession(mediaSession.sessionToken)

        val notification = Notification.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setStyle(mediaStyle)
            .setSmallIcon(R.drawable.ic_play)
            .build()

        startForeground(NOTIFICATION_ID, notification)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int = START_STICKY
    override fun onBind(intent: Intent): IBinder? = null

    override fun onDestroy() {
        super.onDestroy()
        player.release()
        mediaSession.release()
    }
}
