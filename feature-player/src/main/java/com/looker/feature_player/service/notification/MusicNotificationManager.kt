package com.looker.feature_player.service.notification

import android.app.PendingIntent
import android.content.Context
import android.graphics.Bitmap
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.MediaSessionCompat
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import com.looker.components.toBitmap
import com.looker.constants.Constants.NOTIFICATION_CHANNEL_ID
import com.looker.constants.Constants.NOTIFICATION_ID
import com.looker.feature_player.R

class MusicNotificationManager(
	private val context: Context,
	sessionToken: MediaSessionCompat.Token,
	notificationListener: PlayerNotificationManager.NotificationListener,
	private val newSongCallback: () -> Unit
) {

	private val notificationManager: PlayerNotificationManager

	init {
		val mediaController = MediaControllerCompat(context, sessionToken)
		notificationManager =
			PlayerNotificationManager.Builder(context, NOTIFICATION_ID, NOTIFICATION_CHANNEL_ID)
				.setChannelNameResourceId(R.string.notification_channel_name)
				.setChannelDescriptionResourceId(R.string.notification_channel_description)
				.setMediaDescriptionAdapter(DescriptionAdapter(mediaController))
				.setNotificationListener(notificationListener)
				.setSmallIconResourceId(R.drawable.howl)
				.setPlayActionIconResourceId(R.drawable.ic_play)
				.setPauseActionIconResourceId(R.drawable.ic_pause)
				.setPreviousActionIconResourceId(R.drawable.ic_previous)
				.setNextActionIconResourceId(R.drawable.ic_next)
				.build().apply {
					setMediaSessionToken(sessionToken)
					setUseNextActionInCompactView(true)
					setUsePreviousActionInCompactView(true)
					setUseFastForwardAction(false)
					setUseRewindAction(false)
				}
	}

	fun showNotification(player: Player) {
		notificationManager.setPlayer(player)
	}

	private inner class DescriptionAdapter(private val mediaController: MediaControllerCompat) :
		PlayerNotificationManager.MediaDescriptionAdapter {
		override fun getCurrentContentTitle(player: Player): CharSequence {
			newSongCallback()
			return mediaController.metadata.description.title.toString()
		}

		override fun createCurrentContentIntent(player: Player): PendingIntent? {
			return mediaController.sessionActivity
		}

		override fun getCurrentContentText(player: Player): CharSequence? {
			return mediaController.metadata.description.subtitle
		}

		override fun getCurrentLargeIcon(
			player: Player,
			callback: PlayerNotificationManager.BitmapCallback
		): Bitmap? {
			var bitmap: Bitmap? = null
			toBitmap(
				context = context,
				data = mediaController.metadata.description.iconUri.toString()
			) { bitmap = it }
			return bitmap
		}
	}
}