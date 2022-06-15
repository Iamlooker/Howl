package com.looker.core_service.callback

import android.widget.Toast
import com.google.android.exoplayer2.PlaybackException
import com.google.android.exoplayer2.Player
import com.looker.core_service.MusicService

class MusicPlayerEventListener(
	private val musicService: MusicService
) : Player.Listener {

	override fun onPlaybackStateChanged(playbackState: Int) {
		super.onPlaybackStateChanged(playbackState)
		if (playbackState == Player.STATE_READY) {
			musicService.stopForeground(false)
		}
	}

	override fun onPlayerError(error: PlaybackException) {
		super.onPlayerError(error)
		Toast.makeText(musicService, "Error Occurred", Toast.LENGTH_SHORT).show()
	}
}