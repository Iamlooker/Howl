package com.looker.feature_player.queue

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.looker.core_common.combineAndStateIn
import com.looker.core_service.CustomQueueItem
import com.looker.core_service.MusicServiceConnection
import com.looker.core_service.utils.extension.toSong
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.filter
import javax.inject.Inject

@HiltViewModel
class PlayerQueueViewModel @Inject constructor(
	private val musicServiceConnection: MusicServiceConnection
) : ViewModel() {

	private val playbackState = musicServiceConnection.playbackState
	val playerQueue = musicServiceConnection.currentQueue
	val nowPlaying = musicServiceConnection.nowPlaying

	val nextItem = combineAndStateIn(
		playerQueue.filter { it.isNotEmpty() },
		playbackState,
		scope = viewModelScope,
		initialValue = CustomQueueItem(0L, nowPlaying.value.toSong)
	) { queue, current ->
		val currentItem = queue.find { it.queueId == current.activeQueueItemId } ?: queue[0]
		val currentIndex = queue.indexOf(currentItem)
		val nextIndex = queue.elementAtOrNull(currentIndex + 1)
		nextIndex ?: CustomQueueItem(0L, nowPlaying.value.toSong)
	}

	fun playMedia(id: Long) {
		musicServiceConnection.transportControls.skipToQueueItem(id)
	}
}