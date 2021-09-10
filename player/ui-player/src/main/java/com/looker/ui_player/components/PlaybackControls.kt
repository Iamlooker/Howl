package com.looker.ui_player.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.ButtonColors
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.SkipNext
import androidx.compose.material.icons.rounded.SkipPrevious
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.looker.components.ShapedIconButton
import com.looker.components.compositeOverBackground

@Composable
fun PlaybackControls(
    modifier: Modifier = Modifier,
    playIcon: ImageVector,
    onPlayPause: () -> Unit,
    progressValue: Float,
    onSeek: (Float) -> Unit
) {
    val playButtonColors = ButtonDefaults.buttonColors(
        backgroundColor = MaterialTheme.colors.primaryVariant.compositeOverBackground(),
        contentColor = MaterialTheme.colors.primary
    )

    val skipButtonColors =
        ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.surface)

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
            playClick = onPlayPause
        )
        PreviousAndQueue(skipButtonColors = skipButtonColors)
    }
}

@Composable
fun PlayAndSkipButton(
    modifier: Modifier = Modifier,
    playIcon: ImageVector,
    playButtonColors: ButtonColors = ButtonDefaults.buttonColors(),
    skipButtonColors: ButtonColors = ButtonDefaults.buttonColors(),
    playClick: () -> Unit = {},
    skipNextClick: () -> Unit = {}
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
    skipPrevClick: () -> Unit = {}
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
                .weight(3f)
        )
    }
}