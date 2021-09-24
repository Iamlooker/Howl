package com.looker.ui_player.components

import androidx.annotation.FloatRange
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.looker.components.ShapedIconButton
import com.looker.components.compositeOverBackground
import kotlinx.coroutines.launch

@Composable
fun PlaybackControls(
    modifier: Modifier = Modifier,
    isPlaying: Boolean,
    onPlayPause: () -> Unit,
    skipNextClick: () -> Unit,
    skipPrevClick: () -> Unit,
    @FloatRange(from = 0.0, to = 1.0) progressValue: Float,
    onSeek: (Float) -> Unit,
    openQueue: () -> Unit
) {

    var playIcon by remember {
        mutableStateOf(Icons.Rounded.PlayArrow)
    }

    val playButtonColors = ButtonDefaults.buttonColors(
        backgroundColor = MaterialTheme.colors.primary
    )

    val skipButtonColors = ButtonDefaults.buttonColors(
        backgroundColor = MaterialTheme.colors.secondaryVariant.compositeOverBackground()
    )

    LaunchedEffect(isPlaying) {
        launch {
            playIcon = if (isPlaying) Icons.Rounded.Pause else Icons.Rounded.PlayArrow
        }
    }

    Column(modifier) {
        SeekBar(
            modifier = Modifier.padding(horizontal = 20.dp),
            progress = progressValue,
            onValueChanged = onSeek
        )
        PlayAndSkipButton(
            playButtonColors = playButtonColors,
            skipButtonColors = skipButtonColors,
            playIcon = playIcon,
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
    playIcon: ImageVector,
    playButtonColors: ButtonColors = ButtonDefaults.buttonColors(),
    skipButtonColors: ButtonColors = ButtonDefaults.buttonColors(),
    playClick: () -> Unit,
    skipNextClick: () -> Unit
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
                .weight(3f),
            onClick = playClick,
            icon = playIcon,
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