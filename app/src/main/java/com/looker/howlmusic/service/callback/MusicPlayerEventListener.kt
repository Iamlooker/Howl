package com.looker.howlmusic.service.callback

import android.widget.Toast
import com.google.android.exoplayer2.PlaybackException
import com.google.android.exoplayer2.Player
import com.looker.howlmusic.service.MusicService

class MusicPlayerEventListener(
	private val musicService: MusicService
) : Player.Listener {

	var playWhenReady = true

	override fun onPlayWhenReadyChanged(playWhenReady: Boolean, reason: Int) {
		super.onPlayWhenReadyChanged(playWhenReady, reason)
		this.playWhenReady = playWhenReady
	}

	override fun onPlaybackStateChanged(playbackState: Int) {
		super.onPlaybackStateChanged(playbackState)
		if (playbackState == Player.STATE_READY && playWhenReady) {
			musicService.stopForeground(false)
		}
	}

	override fun onPlayerError(error: PlaybackException) {
		super.onPlayerError(error)
		Toast.makeText(musicService, "Error Occurred", Toast.LENGTH_SHORT).show()
	}
}