package com.looker.feature_player.service.callback

import android.app.Notification
import android.content.Intent
import androidx.core.content.ContextCompat
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import com.looker.constants.Constants.NOTIFICATION_ID
import com.looker.feature_player.service.MusicService

class MusicPlayerNotificationListener(
	private val musicService: MusicService
) : PlayerNotificationManager.NotificationListener {
	override fun onNotificationCancelled(notificationId: Int, dismissedByUser: Boolean) {
		super.onNotificationCancelled(notificationId, dismissedByUser)
		musicService.apply {
			stopForeground(true)
			isForegroundServiceOn = false
			stopSelf()
		}
	}

	override fun onNotificationPosted(
		notificationId: Int,
		notification: Notification,
		ongoing: Boolean
	) {
		super.onNotificationPosted(notificationId, notification, ongoing)
		musicService.apply {
			if (ongoing && !isForegroundServiceOn) {
				ContextCompat.startForegroundService(
					this,
					Intent(applicationContext, this::class.java)
				)
				startForeground(NOTIFICATION_ID, notification)
				isForegroundServiceOn = true
			}
		}
	}
}