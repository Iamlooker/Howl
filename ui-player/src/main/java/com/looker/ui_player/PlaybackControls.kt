package com.looker.ui_player

import androidx.compose.animation.core.animateIntAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.SkipNext
import androidx.compose.material.icons.rounded.SkipPrevious
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.looker.components.ShapedIconButton
import com.looker.components.compositeOverBackground
import com.looker.components.localComposers.LocalDurations
import com.looker.components.tweenAnimation

@Composable
fun PlayerControls(
	modifier: Modifier = Modifier,
	isPlaying: Boolean,
	playIcon: ImageVector = Icons.Rounded.PlayArrow,
	onPlayPause: (Boolean) -> Unit,
	skipNextClick: () -> Unit,
	skipPrevClick: () -> Unit,
	progressBar: @Composable () -> Unit
) {
	Column(
		modifier = modifier.padding(20.dp),
		verticalArrangement = Arrangement.spacedBy(20.dp)
	) {
		PlayAndSkipButton(
			isPlaying = isPlaying,
			playClick = onPlayPause,
			skipNextClick = skipNextClick
		) {
			Icon(
				imageVector = playIcon,
				contentDescription = null
			)
		}
		PreviousAndSeekBar(
			skipPrevClick = skipPrevClick,
			progressBar = progressBar
		)
	}
}

@Composable
fun PlayAndSkipButton(
	modifier: Modifier = Modifier,
	isPlaying: Boolean,
	playClick: (Boolean) -> Unit,
	skipNextClick: () -> Unit,
	playIcon: @Composable () -> Unit,
) {
	val buttonShape by animateIntAsState(
		targetValue = if (isPlaying) 50 else 15,
		animationSpec = tweenAnimation(LocalDurations.current.crossFade)
	)

	Row(
		modifier = modifier.fillMaxWidth(),
		horizontalArrangement = Arrangement.spacedBy(20.dp)
	) {
		ShapedIconButton(
			modifier = Modifier
				.height(60.dp)
				.weight(3f)
				.graphicsLayer {
					shape = RoundedCornerShape(buttonShape)
					clip = true
				},
			onClick = { playClick(isPlaying) },
			icon = playIcon,
			backgroundColor = MaterialTheme.colors.primaryVariant.compositeOverBackground(0.9f),
			contentColor = MaterialTheme.colors.onPrimary
		)

		ShapedIconButton(
			modifier = Modifier
				.height(60.dp)
				.weight(1f)
				.clip(CircleShape),
			onClick = skipNextClick,
			backgroundColor = MaterialTheme.colors.secondaryVariant.compositeOverBackground(0.9f),
			contentColor = MaterialTheme.colors.onSecondary
		) {
			Icon(
				imageVector = Icons.Rounded.SkipNext,
				contentDescription = null
			)
		}
	}
}

@Composable
fun PreviousAndSeekBar(
	modifier: Modifier = Modifier,
	skipPrevClick: () -> Unit,
	progressBar: @Composable () -> Unit
) {
	Row(
		modifier = modifier.fillMaxWidth(),
		horizontalArrangement = Arrangement.spacedBy(20.dp)
	) {
		ShapedIconButton(
			modifier = Modifier
				.height(60.dp)
				.weight(0.3f)
				.clip(CircleShape),
			onClick = skipPrevClick,
			backgroundColor = MaterialTheme.colors.secondaryVariant.compositeOverBackground(0.9f),
			contentColor = MaterialTheme.colors.onSecondary
		) {
			Icon(
				imageVector = Icons.Rounded.SkipPrevious,
				contentDescription = null
			)
		}
		progressBar()
	}
}