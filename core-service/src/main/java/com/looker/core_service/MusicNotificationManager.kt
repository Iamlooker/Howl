package com.looker.core_service

import android.app.PendingIntent
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.MediaSessionCompat
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import com.looker.core_common.Constants.NOTIFICATION_CHANNEL_ID
import com.looker.core_common.Constants.NOTIFICATION_ID
import com.looker.core_ui.bitmap
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class MusicNotificationManager(
	private val context: Context,
	sessionToken: MediaSessionCompat.Token,
	notificationListener: PlayerNotificationManager.NotificationListener
) {

	private val notificationManager: PlayerNotificationManager
	private val serviceJob = SupervisorJob()
	private val serviceScope = CoroutineScope(Dispatchers.Main + serviceJob)

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

	fun hideNotification() {
		notificationManager.setPlayer(null)
	}

	fun showNotification(player: Player) {
		notificationManager.setPlayer(player)
	}

	private inner class DescriptionAdapter(private val mediaController: MediaControllerCompat) :
		PlayerNotificationManager.MediaDescriptionAdapter {

		var currentIconUri: Uri? = null
		var currentBitmap: Bitmap? = null

		override fun getCurrentContentTitle(player: Player): CharSequence =
			mediaController.metadata.description.title.toString()

		override fun createCurrentContentIntent(player: Player): PendingIntent? =
			mediaController.sessionActivity

		override fun getCurrentContentText(player: Player): CharSequence? =
			mediaController.metadata.description.subtitle

		override fun getCurrentLargeIcon(
			player: Player,
			callback: PlayerNotificationManager.BitmapCallback
		): Bitmap? {
			val iconUri = mediaController.metadata.description.iconUri
			return if (currentIconUri != iconUri || currentBitmap == null) {
				currentIconUri = iconUri
				serviceScope.launch {
					currentBitmap = iconUri?.let { retrieveBitmap(it) }
					currentBitmap?.let { callback.onBitmap(it) }
				}
				null
			} else currentBitmap
		}

		private suspend fun retrieveBitmap(uri: Uri): Bitmap = uri.toString().bitmap(context)
	}
}