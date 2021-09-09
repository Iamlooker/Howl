package com.looker.ui_player

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ButtonDefaults.buttonColors
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Slider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.SkipNext
import androidx.compose.material.icons.rounded.SkipPrevious
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import com.google.accompanist.insets.statusBarsPadding
import com.looker.components.CircleIconButton
import com.looker.components.HowlImage
import com.looker.components.HowlSurface
import com.looker.components.WrappedText

@Composable
fun Player(
    modifier: Modifier = Modifier,
    songName: String,
    artistName: String,
    albumArt: Any
) {
    HowlSurface(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {

        ConstraintLayout(constraintSet = playerConstraints(20.dp)) {
            HowlImage(
                modifier = Modifier
                    .size(300.dp)
                    .layoutId("albumArt"),
                data = albumArt,
                shape = CircleShape
            )
            SongText(
                modifier = modifier.layoutId("songDetails"),
                songName = songName,
                artistName = artistName
            )
            PlaybackControls(modifier = modifier.layoutId("controls"))
        }
    }
}

private fun playerConstraints(margin: Dp): ConstraintSet = ConstraintSet {
    val albumArt = createRefFor("albumArt")
    val songDetails = createRefFor("songDetails")
    val controls = createRefFor("controls")

    constrain(albumArt) {
        top.linkTo(parent.top, margin = margin)
        centerHorizontallyTo(parent)
    }
    constrain(songDetails) {
        top.linkTo(albumArt.bottom, margin = margin)
        bottom.linkTo(controls.top, margin = margin)
        centerHorizontallyTo(parent)
    }
    constrain(controls) {
        bottom.linkTo(parent.bottom)
        centerHorizontallyTo(parent)
        width = Dimension.fillToConstraints
    }
}

private fun controlsConstraints(margin: Dp): ConstraintSet = ConstraintSet {
    val playButton = createRefFor("playButton")
    val skipNextButton = createRefFor("skipNextButton")
    val skipPrevButton = createRefFor("skipPrevButton")
    val progressBar = createRefFor("progressBar")

    constrain(playButton) {
        start.linkTo(parent.start, margin = margin)
        end.linkTo(skipNextButton.start, margin = margin)
        bottom.linkTo(skipPrevButton.top, margin = margin)
        width = Dimension.fillToConstraints
        height = Dimension.value(60.dp)
    }
    constrain(skipNextButton) {
        end.linkTo(parent.end, margin = margin)
        bottom.linkTo(progressBar.top, margin = margin)
        height = Dimension.value(60.dp)
    }
    constrain(skipPrevButton) {
        start.linkTo(parent.start, margin = margin)
        bottom.linkTo(parent.bottom, margin = margin * 2)
        height = Dimension.value(60.dp)
    }
    constrain(progressBar) {
        start.linkTo(skipPrevButton.end, margin = margin)
        end.linkTo(parent.end, margin = margin)
        bottom.linkTo(parent.bottom, margin = margin * 2)
        width = Dimension.fillToConstraints
        height = Dimension.value(60.dp)
    }
}

@Composable
fun PlaybackControls(modifier: Modifier = Modifier) {
    val playButtonColors = buttonColors(backgroundColor = MaterialTheme.colors.primary)

    val skipButtonColors = buttonColors(backgroundColor = MaterialTheme.colors.surface)

    var progressValue by remember { mutableStateOf(0f) }

    val progress by animateFloatAsState(targetValue = progressValue)

    ConstraintLayout(modifier = modifier, constraintSet = controlsConstraints(20.dp)) {
        CircleIconButton(
            modifier = Modifier.layoutId("playButton"),
            onClick = {},
            icon = Icons.Rounded.PlayArrow,
            buttonColors = playButtonColors,
            contentDescription = "play"
        )

        CircleIconButton(
            modifier = Modifier.layoutId("skipNextButton"),
            onClick = { progressValue = progressValue.seekForward() },
            icon = Icons.Rounded.SkipNext,
            buttonColors = skipButtonColors,
            contentDescription = "next"
        )

        CircleIconButton(
            modifier = Modifier.layoutId("skipPrevButton"),
            onClick = { progressValue = progressValue.seekBack() },
            icon = Icons.Rounded.SkipPrevious,
            buttonColors = skipButtonColors,
            contentDescription = "previous"
        )

        SeekBar(
            modifier = Modifier.layoutId("progressBar"),
            progress = progress,
            onValueChanged = { progressValue = it }
        )
    }
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
    Slider(
        modifier = modifier,
        value = progress,
        onValueChange = onValueChanged
    )
}

fun Float.seekForward(by: Float = 0.1f): Float =
    if (this.plus(by) >= 1f) 1f
    else this.plus(by)

fun Float.seekBack(by: Float = 0.1f): Float =
    if (this.minus(by) <= 0f) 0f
    else this.minus(by)