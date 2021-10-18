package com.looker.ui_player

import androidx.annotation.FloatRange
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.SkipNext
import androidx.compose.material.icons.rounded.SkipPrevious
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.looker.components.ShapedIconButton
import com.looker.components.compositeOverBackground
import com.looker.components.state.PlayState
import com.looker.components.state.PlayState.PAUSED
import com.looker.components.state.PlayState.PLAYING
import com.looker.components.tweenAnimation
import com.looker.ui_player.components.QueueHeader
import com.looker.ui_player.components.SeekBar
import kotlinx.coroutines.launch

@Composable
fun PlayerControls(
    modifier: Modifier = Modifier,
    isPlaying: PlayState,
    @FloatRange(from = 0.0, to = 1.0) progressValue: Float,
    openQueue: () -> Unit,
    onSeek: (Float) -> Unit,
    onPlayPause: (PlayState) -> Unit,
    skipNextClick: () -> Unit,
    skipPrevClick: () -> Unit
) {
    Column(
        modifier = modifier.padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        SeekBar(
            progress = progressValue,
            onValueChanged = onSeek
        )
        PlayAndSkipButton(
            isPlaying = isPlaying,
            playClick = onPlayPause,
            skipNextClick = skipNextClick
        )
        PreviousAndQueue(
            openQueue = openQueue,
            skipPrevClick = skipPrevClick
        )
    }
}

@Composable
fun PlayAndSkipButton(
    modifier: Modifier = Modifier,
    isPlaying: PlayState,
    playClick: (PlayState) -> Unit,
    skipNextClick: () -> Unit
) {
    var cornerRadius by remember { mutableStateOf(25) }
    var playIcon by remember(isPlaying) { mutableStateOf(Icons.Rounded.PlayArrow) }
    val iconShape by animateIntAsState(
        targetValue = cornerRadius,
        animationSpec = tweenAnimation()
    )

    LaunchedEffect(isPlaying) {
        launch {
            playIcon = when (isPlaying) {
                PLAYING -> Icons.Rounded.Pause
                PAUSED -> Icons.Rounded.PlayArrow
            }
            cornerRadius = when (isPlaying) {
                PLAYING -> 50
                PAUSED -> 15
            }
        }
    }

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        ShapedIconButton(
            modifier = Modifier
                .height(60.dp)
                .weight(3f),
            onClick = { playClick(isPlaying) },
            icon = playIcon,
            shape = RoundedCornerShape(iconShape),
            backgroundColor = MaterialTheme.colors.primaryVariant.compositeOverBackground(0.9f),
            contentColor = MaterialTheme.colors.onPrimary,
            contentDescription = "Play Pause Song"
        )

        ShapedIconButton(
            modifier = Modifier
                .height(60.dp)
                .weight(1f),
            onClick = skipNextClick,
            icon = Icons.Rounded.SkipNext,
            backgroundColor = MaterialTheme.colors.secondaryVariant.compositeOverBackground(0.9f),
            contentColor = MaterialTheme.colors.onSecondary,
            contentDescription = "Play Next Song"
        )
    }
}

@Composable
fun PreviousAndQueue(
    modifier: Modifier = Modifier,
    skipPrevClick: () -> Unit,
    openQueue: () -> Unit
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        ShapedIconButton(
            modifier = Modifier
                .height(60.dp)
                .weight(1f),
            onClick = skipPrevClick,
            icon = Icons.Rounded.SkipPrevious,
            backgroundColor = MaterialTheme.colors.secondaryVariant.compositeOverBackground(0.9f),
            contentColor = MaterialTheme.colors.onSecondary,
            contentDescription = "Play Previous Song"
        )
        QueueHeader(
            modifier = Modifier
                .height(60.dp)
                .weight(3f),
            openQueue = openQueue
        )
    }
}