package com.looker.player_service.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import androidx.core.app.NotificationCompat
import androidx.media.session.MediaButtonReceiver
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

    lateinit var mediaSession: MediaSessionCompat

    override fun onCreate() {
        super.onCreate()

        mediaSession = MediaSessionCompat(this, "howlmusic")

        val notification =
            NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                .setStyle(
                    androidx.media.app.NotificationCompat.MediaStyle()
                        .setMediaSession(mediaSession.sessionToken)
                        .setShowCancelButton(true)
                        .setCancelButtonIntent(
                            MediaButtonReceiver.buildMediaButtonPendingIntent(
                                this,
                                PlaybackStateCompat.ACTION_STOP
                            )
                        )
                )
                .setSmallIcon(R.drawable.ic_play)

        startForeground(NOTIFICATION_ID, notification.build())
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int = START_STICKY
    override fun onBind(intent: Intent): IBinder? = null

    override fun onDestroy() {
        super.onDestroy()
        player.release()
        mediaSession.release()
    }
}
