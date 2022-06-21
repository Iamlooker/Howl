package com.looker.feature_player.queue

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.looker.core_common.combineAndStateIn
import com.looker.core_model.Song
import com.looker.core_service.MusicServiceConnection
import com.looker.core_service.utils.extension.playPauseMedia
import com.looker.core_service.utils.extension.toMediaMetadataCompat
import com.looker.core_service.utils.extension.toSong
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.filter
import javax.inject.Inject

@HiltViewModel
class PlayerQueueViewModel @Inject constructor(
	private val musicServiceConnection: MusicServiceConnection
) : ViewModel() {

	val playerQueue = musicServiceConnection.currentQueue
	val nowPlaying = musicServiceConnection.nowPlaying

	val nextItem = combineAndStateIn(
		playerQueue.filter { it.isNotEmpty() },
		nowPlaying,
		scope = viewModelScope,
		initialValue = nowPlaying.value.toSong
	) { queue, current ->
		val next = queue.find { it.mediaId == current.toSong.mediaId } ?: queue[0]
		val nextIndex = queue.lastIndexOf(next) + 1
		queue[nextIndex]
	}

	fun playMedia(song: Song) {
		song.toMediaMetadataCompat.playPauseMedia(
			musicServiceConnection = musicServiceConnection,
			canPause = false
		)
	}
}