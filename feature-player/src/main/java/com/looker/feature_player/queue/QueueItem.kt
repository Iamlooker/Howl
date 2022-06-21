package com.looker.feature_player.queue

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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