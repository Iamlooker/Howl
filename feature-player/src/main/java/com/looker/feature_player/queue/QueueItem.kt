package com.looker.feature_player.queue

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.MusicNote
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.looker.core_ui.TitleSubText
import com.looker.core_ui.overBackground

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun QueueItem(
	modifier: Modifier = Modifier,
	title: String,
	subtitle: String,
	isPlaying: Boolean = false,
	onClick: () -> Unit
) {
	val backgroundColor by animateColorAsState(
		targetValue = if (isPlaying) MaterialTheme.colors.primary.overBackground()
		else MaterialTheme.colors.background
	)
	Surface(
		modifier = modifier.fillMaxWidth().padding(horizontal = 8.dp),
		color = backgroundColor,
		shape = MaterialTheme.shapes.large,
		onClick = onClick
	) {
		TitleSubText(
			modifier = Modifier.padding(8.dp),
			title = title,
			subText = subtitle,
			itemTextAlignment = Alignment.Start
		)
	}
}

@Composable
fun CollapsedQueue(
	modifier: Modifier = Modifier,
	songName: @Composable () -> Unit
) {
	Row(
		modifier = modifier
			.widthIn(Dp.Hairline, 350.dp)
			.animateContentSize()
			.padding(8.dp),
		verticalAlignment = Alignment.CenterVertically,
		horizontalArrangement = Arrangement.Center
	) {
		Icon(imageVector = Icons.Rounded.MusicNote, contentDescription = null)
		Spacer(modifier = Modifier.width(8.dp))
		songName()
	}
}

@Composable
fun ExpandedQueue(
	modifier: Modifier = Modifier,
	onBackPressed: () -> Unit,
	songsList: LazyListScope.() -> Unit
) {
	Scaffold(
		modifier = modifier.clip(MaterialTheme.shapes.large),
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
