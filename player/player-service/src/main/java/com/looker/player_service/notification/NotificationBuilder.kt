package com.looker.player_service.notification

import android.app.Notification
import android.app.Notification.VISIBILITY_PUBLIC
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_DEFAULT
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Icon
import android.media.session.MediaSession
import android.media.session.PlaybackState.*
import androidx.annotation.DrawableRes
import androidx.core.app.NotificationCompat.CATEGORY_SERVICE
import com.looker.constants.Constants.NOTIFICATION_CHANNEL_ID
import com.looker.constants.Constants.NOTIFICATION_CHANNEL_NAME
import com.looker.constants.Constants.NOTIFICATION_ID
import com.looker.domain_music.Song
import com.looker.player_service.R

class NotificationBuilder(
    context: Context,
    mediaToken: MediaSession.Token,
    currentSong: Song?,
) : Notification.Builder(context, NOTIFICATION_CHANNEL_ID) {

    private val pendingIntent = PendingIntent.getService(
        context,
        NOTIFICATION_ID,
        Intent(),
        FLAG_IMMUTABLE
    )

    init {
        setSmallIcon(R.drawable.ic_play)
        setCategory(CATEGORY_SERVICE)
        setShowWhen(false)
        setContentTitle(currentSong?.songName)
        setContentText(currentSong?.artistName)
        setContentIntent(pendingIntent)
        setVisibility(VISIBILITY_PUBLIC)
        addAction(buildAction(context, ACTION_SKIP_TO_PREVIOUS.toString(), R.drawable.ic_previous))
        addAction(buildPlayPauseAction(context, false))
        addAction(buildAction(context, ACTION_SKIP_TO_NEXT.toString(), R.drawable.ic_next))

        style = Notification.MediaStyle().setMediaSession(mediaToken)
    }

    private fun buildPlayPauseAction(
        context: Context,
        isPlaying: Boolean,
    ): Notification.Action {
        val drawableRes = if (isPlaying) R.drawable.ic_pause else R.drawable.ic_play

        return buildAction(context, ACTION_PLAY_PAUSE.toString(), drawableRes)
    }


    private fun buildAction(
        context: Context,
        actionName: String,
        @DrawableRes iconRes: Int,
    ): Notification.Action {
        val action = Notification.Action.Builder(
            Icon.createWithResource(context, iconRes), actionName, pendingIntent
        )
        return action.build()
    }

    companion object {

        fun from(
            context: Context,
            notificationManager: NotificationManager,
            mediaSession: MediaSession,
            currentSong: Song?,
        ): NotificationBuilder {
            val channel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID, NOTIFICATION_CHANNEL_NAME,
                IMPORTANCE_DEFAULT
            )

            notificationManager.createNotificationChannel(channel)

            return NotificationBuilder(context, mediaSession.sessionToken, currentSong)
        }
    }
}
