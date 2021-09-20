package com.looker.player_service.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.app.NotificationCompat
import com.google.android.exoplayer2.SimpleExoPlayer
import com.looker.constants.Constants.NOTIFICATION_CHANNEL_ID
import com.looker.constants.Constants.NOTIFICATION_ID
import com.looker.domain_music.emptySong
import com.looker.player_service.R
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import androidx.media.app.NotificationCompat as MediaAppNotificationCompat

@AndroidEntryPoint
class PlayerService : Service() {

    @Inject
    lateinit var player: SimpleExoPlayer

    lateinit var mediaSession: MediaSessionCompat
    lateinit var notification: NotificationCompat.Builder
    var currentSong by mutableStateOf(emptySong)

    override fun onCreate() {
        super.onCreate()

        mediaSession = MediaSessionCompat(this, "howlmusic")
        mediaSession.setMetadata(
            MediaMetadataCompat.Builder()
                .putString(MediaMetadataCompat.METADATA_KEY_ALBUM_ART_URI, currentSong.albumArt)
                .putString(MediaMetadataCompat.METADATA_KEY_TITLE, currentSong.songName)
                .putString(MediaMetadataCompat.METADATA_KEY_ARTIST, currentSong.artistName)
                .build()
        )
        notification = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID).apply {
            setStyle(
                MediaAppNotificationCompat.MediaStyle()
                    .setMediaSession(mediaSession.sessionToken)
            )
            setSmallIcon(R.drawable.ic_play)
        }
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
