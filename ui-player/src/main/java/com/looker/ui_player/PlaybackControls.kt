package com.looker.ui_player

import androidx.annotation.FloatRange
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.looker.ui_player.components.SeekBar

@Composable
fun PlayerControls(
	modifier: Modifier = Modifier,
	isPlaying: Boolean,
	@FloatRange(from = 0.0, to = 1.0) progressValue: Float,
	playIcon: ImageVector = Icons.Rounded.PlayArrow,
	onSeek: (Float) -> Unit,
	onPlayPause: (Boolean) -> Unit,
	skipNextClick: () -> Unit,
	skipPrevClick: () -> Unit,
) {
	Column(
		modifier = modifier.padding(20.dp),
		verticalArrangement = Arrangement.spacedBy(20.dp)
	) {
		PlayAndSkipButton(
			isPlaying = isPlaying,
			playIcon = playIcon,
			playClick = onPlayPause,
			skipNextClick = skipNextClick
		)
		PreviousAndSeekBar(
			progress = progressValue,
			skipPrevClick = skipPrevClick,
			onSeek = onSeek
		)
	}
}

@Composable
fun PlayAndSkipButton(
	modifier: Modifier = Modifier,
	isPlaying: Boolean,
	playIcon: ImageVector,
	playClick: (Boolean) -> Unit,
	skipNextClick: () -> Unit,
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
			contentColor = MaterialTheme.colors.onPrimary,
			contentDescription = "Play Pause Song"
		)

		ShapedIconButton(
			modifier = Modifier
				.height(60.dp)
				.weight(1f)
				.clip(CircleShape),
			onClick = skipNextClick,
			icon = Icons.Rounded.SkipNext,
			backgroundColor = MaterialTheme.colors.secondaryVariant.compositeOverBackground(0.9f),
			contentColor = MaterialTheme.colors.onSecondary,
			contentDescription = "Play Next Song"
		)
	}
}

@Composable
fun PreviousAndSeekBar(
	modifier: Modifier = Modifier,
	progress: Float,
	skipPrevClick: () -> Unit,
	onSeek: (Float) -> Unit,
) {
	Row(
		modifier = modifier.fillMaxWidth(),
		horizontalArrangement = Arrangement.spacedBy(20.dp)
	) {
		ShapedIconButton(
			modifier = Modifier
				.height(60.dp)
				.weight(1f)
				.clip(CircleShape),
			onClick = skipPrevClick,
			icon = Icons.Rounded.SkipPrevious,
			backgroundColor = MaterialTheme.colors.secondaryVariant.compositeOverBackground(0.9f),
			contentColor = MaterialTheme.colors.onSecondary,
			contentDescription = "Play Previous Song"
		)
		SeekBar(
			modifier = Modifier
				.height(60.dp)
				.weight(3f),
			progress = progress,
			onValueChanged = onSeek
		)
	}
}