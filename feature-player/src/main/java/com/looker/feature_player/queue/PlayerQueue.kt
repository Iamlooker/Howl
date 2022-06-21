package com.looker.feature_player.queue

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.MusicNote
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.looker.core_service.utils.extension.toSong
import com.looker.core_ui.SongUiState
import com.looker.core_ui.basicSongsList

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun PlayerQueue(viewModel: PlayerQueueViewModel = hiltViewModel()) {
	var isExpanded by remember { mutableStateOf(false) }
	val nowPlaying by viewModel.nowPlaying.collectAsState()
	val nextItem by viewModel.nextItem.collectAsState()
	val queue by viewModel.playerQueue.collectAsState()
	AnimatedContent(targetState = isExpanded) { currentState ->
		if (currentState) {
			ExpandedQueue(
				modifier = Modifier.fillMaxSize(),
				onBackPressed = { isExpanded = false }
			) {
				basicSongsList(SongUiState.Success(queue)) { currentSong ->
					QueueItem(
						title = currentSong.name,
						subtitle = currentSong.artist,
						isPlaying = nowPlaying.toSong.mediaId == currentSong.mediaId
					) { viewModel.playMedia(currentSong) }
				}
			}
		} else {
			CollapsedQueue(onClick = { isExpanded = true }) {
				Text(
					text = nextItem.name,
					maxLines = 1,
					overflow = TextOverflow.Ellipsis
				)
			}
		}
	}
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CollapsedQueue(
	modifier: Modifier = Modifier,
	onClick: () -> Unit,
	songName: @Composable () -> Unit
) {
	Surface(
		modifier = modifier.widthIn(Dp.Hairline, 350.dp),
		color = MaterialTheme.colors.secondaryVariant,
		shape = CircleShape,
		onClick = onClick
	) {
		Row(
			modifier = Modifier.animateContentSize().padding(8.dp),
			verticalAlignment = Alignment.CenterVertically,
			horizontalArrangement = Arrangement.Center
		) {
			Icon(imageVector = Icons.Rounded.MusicNote, contentDescription = null)
			Spacer(modifier = Modifier.width(8.dp))
			songName()
		}
	}
}

@Composable
fun ExpandedQueue(
	modifier: Modifier = Modifier,
	onBackPressed: () -> Unit,
	songsList: LazyListScope.() -> Unit
) {
	Scaffold(
		modifier = modifier,
		topBar = {
			Surface(
				modifier = Modifier.fillMaxWidth(),
				color = MaterialTheme.colors.background
			) {
				Box {
					IconButton(
						modifier = Modifier.align(Alignment.CenterStart),
						onClick = onBackPressed
					) {
						Icon(imageVector = Icons.Rounded.ArrowBack, contentDescription = "Go Back")
					}
					Text(
						modifier = Modifier.align(Alignment.Center),
						text = "Now Playing",
						style = MaterialTheme.typography.h5
					)
				}
			}
		}
	) {
		LazyColumn(contentPadding = it, content = songsList)
	}
}