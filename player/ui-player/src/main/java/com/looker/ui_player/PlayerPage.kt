package com.looker.ui_player

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ButtonColors
import androidx.compose.material.ButtonDefaults.buttonColors
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Slider
import androidx.compose.material.SliderDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Shuffle
import androidx.compose.material.icons.rounded.SkipNext
import androidx.compose.material.icons.rounded.SkipPrevious
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.statusBarsPadding
import com.looker.components.*
import com.looker.components.ComponentConstants.tweenAnimation

@Composable
fun Player(
    modifier: Modifier = Modifier,
    songName: String,
    artistName: String,
    albumArt: Any
) {
    HowlSurface(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {
        Column(
            modifier = modifier,
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AlbumArt(modifier = Modifier.size(400.dp, 300.dp), albumArt = albumArt)
            SongText(songName = songName, artistName = artistName)
            Spacer(modifier = Modifier.height(50.dp))
            PlaybackControls(modifier = Modifier.fillMaxWidth())
        }
    }
}

@Composable
fun PlaybackControls(modifier: Modifier = Modifier) {
    val playButtonColors = buttonColors(
        backgroundColor = MaterialTheme.colors.primaryVariant.copy(0.4f)
            .compositeOver(MaterialTheme.colors.background),
        contentColor = MaterialTheme.colors.primary
    )

    val skipButtonColors = buttonColors(backgroundColor = MaterialTheme.colors.surface)

    var progressValue by remember { mutableStateOf(0f) }

    val progress by animateFloatAsState(targetValue = progressValue)

    Column(modifier) {
        SeekBar(
            modifier = Modifier.padding(horizontal = 20.dp),
            progress = progress,
            onValueChanged = { progressValue = it }
        )
        PlayAndSkipButton(
            playButtonColors = playButtonColors,
            skipButtonColors = skipButtonColors,
            skipNextClick = { progressValue = progressValue.seekForward() }
        )
        PreviousAndQueue(skipButtonColors = skipButtonColors) {
            progressValue = progressValue.seekBack()
        }
    }
}

@Composable
fun AlbumArt(modifier: Modifier = Modifier, albumArt: Any) {
    Box {
        HowlImage(
            modifier = modifier,
            data = albumArt,
            shape = CircleShape
        )
        ShuffleButton(modifier = Modifier.align(Alignment.BottomEnd))
    }
}

@Composable
fun ShuffleButton(modifier: Modifier = Modifier) {

    var shuffle by remember {
        mutableStateOf(false)
    }

    val shuffleColor by animateColorAsState(
        targetValue = if (shuffle) MaterialTheme.colors.secondary.compositeOverBackground()
        else MaterialTheme.colors.surface, animationSpec = tweenAnimation()
    )

    val shuffleButtonColors = buttonColors(backgroundColor = shuffleColor)

    ShapedIconButton(
        modifier = modifier.padding(20.dp),
        icon = Icons.Rounded.Shuffle,
        buttonColors = shuffleButtonColors,
        onClick = { shuffle = !shuffle },
        contentDescription = "Shuffle Button"
    )
}

@Composable
fun SongText(
    modifier: Modifier = Modifier,
    songName: String,
    artistName: String
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        WrappedText(
            text = songName,
            style = MaterialTheme.typography.h5
        )
        WrappedText(
            text = artistName,
            style = MaterialTheme.typography.subtitle1
        )
    }
}

@Composable
fun SeekBar(
    modifier: Modifier = Modifier,
    progress: Float,
    onValueChanged: (Float) -> Unit
) {
    val sliderColors = SliderDefaults.colors(
        inactiveTrackColor = MaterialTheme.colors.surface
    )

    Slider(
        modifier = modifier,
        value = progress,
        onValueChange = onValueChanged,
        colors = sliderColors
    )
}

@Composable
fun PlayAndSkipButton(
    modifier: Modifier = Modifier,
    playButtonColors: ButtonColors = buttonColors(),
    skipButtonColors: ButtonColors = buttonColors(),
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
            icon = Icons.Rounded.PlayArrow,
            buttonColors = playButtonColors,
            contentDescription = "play"
        )

        ShapedIconButton(
            modifier = Modifier
                .height(60.dp)
                .weight(1f),
            onClick = skipNextClick,
            icon = Icons.Rounded.SkipNext,
            buttonColors = skipButtonColors,
            contentDescription = "next"
        )
    }
}

@Composable
fun PreviousAndQueue(
    modifier: Modifier = Modifier,
    skipButtonColors: ButtonColors = buttonColors(),
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
            contentDescription = "previous"
        )
        QueueHeader(
            modifier = Modifier
                .height(60.dp)
                .weight(3f)
        )
    }
}

@Composable
fun QueueHeader(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(CircleShape)
            .background(MaterialTheme.colors.primaryVariant.compositeOverBackground()),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.matchParentSize(),
            verticalArrangement = Arrangement.SpaceAround,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            WrappedText(
                text = "UpNext",
                textColor = MaterialTheme.colors.primary,
                style = MaterialTheme.typography.subtitle1
            )
            WrappedText(
                text = "Next Song Name",
                style = MaterialTheme.typography.caption
            )
        }
    }
}

fun Float.seekForward(by: Float = 0.1f): Float =
    if (this.plus(by) >= 1f) 1f
    else this.plus(by)

fun Float.seekBack(by: Float = 0.1f): Float =
    if (this.minus(by) <= 0f) 0f
    else this.minus(by)