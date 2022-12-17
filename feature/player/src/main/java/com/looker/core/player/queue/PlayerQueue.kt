package com.looker.core.player.queue

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.hilt.navigation.compose.hiltViewModel
import com.looker.core.service.CustomQueueItem
import com.looker.core.service.extensions.toSong

@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterialApi::class)
@Composable
fun PlayerQueue(viewModel: PlayerQueueViewModel = hiltViewModel()) {
	var isExpanded by remember { mutableStateOf(false) }
	val nowPlaying by viewModel.nowPlaying.collectAsState()
	val nextItem by viewModel.nextItem.collectAsState()
	val queue by viewModel.playerQueue.collectAsState()

	Surface(
		color = MaterialTheme.colors.secondaryVariant,
		shape = MaterialTheme.shapes.large,
		onClick = { if (!isExpanded) isExpanded = true }
	) {
		AnimatedContent(targetState = isExpanded) { currentState ->
			if (currentState) {
				ExpandedQueue(
					modifier = Modifier.fillMaxSize(),
					onBackPressed = { isExpanded = false }
				) {
					queueList(queue) { currentSong ->
						QueueItem(
							title = currentSong.song.name,
							subtitle = currentSong.song.artist,
							isPlaying = nowPlaying.toSong.mediaId == currentSong.song.mediaId
						) { viewModel.playMedia(currentSong.queueId) }
					}
				}
			} else {
				CollapsedQueue {
					Text(
						text = nextItem.song.name,
						maxLines = 1,
						style = MaterialTheme.typography.caption,
						overflow = TextOverflow.Ellipsis
					)
				}
			}
		}
	}
}

fun LazyListScope.queueList(
	queue: List<CustomQueueItem>,
	item: @Composable (CustomQueueItem) -> Unit
) {
	items(queue) { item(it) }
}