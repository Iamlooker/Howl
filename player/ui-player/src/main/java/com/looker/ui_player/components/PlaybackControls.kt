package com.looker.ui_player.components

import androidx.annotation.FloatRange
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonColors
import androidx.compose.material.ButtonDefaults
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
import kotlinx.coroutines.launch

@Composable
fun PlaybackControls(
    modifier: Modifier = Modifier,
    isPlaying: PlayState,
    @FloatRange(from = 0.0, to = 1.0) progressValue: Float,
    openQueue: () -> Unit,
    onSeek: (Float) -> Unit,
    onPlayPause: (PlayState) -> Unit,
    skipNextClick: () -> Unit,
    skipPrevClick: () -> Unit
) {


    val playButtonColors = ButtonDefaults.buttonColors(
        MaterialTheme.colors.primary.compositeOverBackground(0.6f)
    )

    val skipButtonColors = ButtonDefaults.buttonColors(
        MaterialTheme.colors.secondaryVariant.compositeOverBackground()
    )

    Column(modifier) {
        SeekBar(
            modifier = Modifier.padding(horizontal = 20.dp),
            progress = progressValue,
            onValueChanged = onSeek
        )
        PlayAndSkipButton(
            playButtonColors = playButtonColors,
            skipButtonColors = skipButtonColors,
            isPlaying = isPlaying,
            playClick = onPlayPause,
            skipNextClick = skipNextClick
        )
        PreviousAndQueue(
            skipButtonColors = skipButtonColors,
            openQueue = openQueue,
            skipPrevClick = skipPrevClick
        )
    }
}

@Composable
fun PlayAndSkipButton(
    modifier: Modifier = Modifier,
    isPlaying: PlayState,
    playButtonColors: ButtonColors = ButtonDefaults.buttonColors(),
    skipButtonColors: ButtonColors = ButtonDefaults.buttonColors(),
    playClick: (PlayState) -> Unit,
    skipNextClick: () -> Unit
) {

    val cornerRadius = remember { mutableStateOf(25) }

    val playIcon = remember(isPlaying) { mutableStateOf(Icons.Rounded.PlayArrow) }
    val iconShape by animateIntAsState(
        targetValue = cornerRadius.value,
        animationSpec = tweenAnimation()
    )

    LaunchedEffect(isPlaying) {
        launch {
            playIcon.value = when (isPlaying) {
                PLAYING -> Icons.Rounded.Pause
                PAUSED -> Icons.Rounded.PlayArrow
            }
            cornerRadius.value = when (isPlaying) {
                PLAYING -> 50
                PAUSED -> 15
            }
        }
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(20.dp),
        horizontalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        ShapedIconButton(
            modifier = Modifier
                .height(60.dp)
                .weight(3f),
            onClick = { playClick(isPlaying) },
            icon = playIcon.value,
            shape = RoundedCornerShape(iconShape),
            buttonColors = playButtonColors,
            contentDescription = "Play Pause Song"
        )

        ShapedIconButton(
            modifier = Modifier
                .height(60.dp)
                .weight(1f),
            onClick = skipNextClick,
            icon = Icons.Rounded.SkipNext,
            buttonColors = skipButtonColors,
            contentDescription = "Play Next Song"
        )
    }
}

@Composable
fun PreviousAndQueue(
    modifier: Modifier = Modifier,
    skipButtonColors: ButtonColors = ButtonDefaults.buttonColors(),
    skipPrevClick: () -> Unit,
    openQueue: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(20.dp),
        horizontalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        ShapedIconButton(
            modifier = Modifier
                .height(60.dp)
                .weight(1f),
            onClick = skipPrevClick,
            icon = Icons.Rounded.SkipPrevious,
            buttonColors = skipButtonColors,
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