package com.looker.core_service.extensions

import android.support.v4.media.MediaMetadataCompat
import com.looker.core_model.Song
import com.looker.core_service.MusicServiceConnection

inline fun MediaMetadataCompat.playPauseMedia(
	musicServiceConnection: MusicServiceConnection,
	canPause: Boolean = true,
	playNewOrOld: () -> Boolean = { false },
	onFailure: () -> Unit = {}
) = toSong.playPauseMedia(musicServiceConnection, canPause, playNewOrOld, onFailure)

inline fun Song.playPauseMedia(
	musicServiceConnection: MusicServiceConnection,
	canPause: Boolean = true,
	playNewOrOld: () -> Boolean = { false },
	onFailure: () -> Unit = {}
) {
	val transportControls = musicServiceConnection.transportControls
	val playbackStateCompat = musicServiceConnection.playbackState.value
	val isPrepared = playbackStateCompat.isPrepared

	if (isPrepared && playNewOrOld()) {
		playbackStateCompat.let { playbackState ->
			when {
				playbackState.isPlaying -> if (canPause) transportControls.pause()
				playbackState.isPlayEnabled -> transportControls.play()
				else -> onFailure()
			}
		}
	} else if (mediaId.isNotEmpty() or mediaId.isNotBlank()) {
		transportControls.playFromMediaId(mediaId, null)
	}
}