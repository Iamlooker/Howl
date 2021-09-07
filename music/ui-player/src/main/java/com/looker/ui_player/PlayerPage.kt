package com.looker.ui_player

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import com.looker.components.ButtonWithIcon
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
    HowlSurface(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            HowlImage(
                modifier = Modifier.size(300.dp),
                data = albumArt,
                shape = CircleShape
            )
            SongText(songName = songName, artistName = artistName)
            Spacer(Modifier.height(80.dp))
            PlaybackControls()
        }
    }
}

@Composable
fun SongText(
    songName: String,
    artistName: String
) {
    Column(
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

private fun playerConstraints(margin: Dp): ConstraintSet {
    return ConstraintSet {
        val playButton = createRefFor("playButton")
        val skipNextButton = createRefFor("skipNextButton")
        val skipPrevButton = createRefFor("skipPrevButton")
        val progressBar = createRefFor("progressBar")

        constrain(playButton) {
            start.linkTo(parent.start, margin = margin)
            end.linkTo(skipNextButton.start, margin = margin)
            width = Dimension.fillToConstraints
            height = Dimension.value(60.dp)
        }
        constrain(skipNextButton) {
            end.linkTo(parent.end, margin = margin)
            height = Dimension.value(60.dp)
        }
        constrain(skipPrevButton) {
            start.linkTo(parent.start, margin = margin)
            top.linkTo(playButton.bottom, margin = margin)
            bottom.linkTo(parent.bottom, margin = margin * 2)
            height = Dimension.value(60.dp)
        }
        constrain(progressBar) {
            top.linkTo(skipNextButton.bottom, margin = margin)
            start.linkTo(skipPrevButton.end, margin = margin)
            end.linkTo(parent.end, margin = margin)
            bottom.linkTo(parent.bottom, margin = margin * 2)
            width = Dimension.fillToConstraints
        }
    }
}


@Composable
fun PlaybackControls() {

    val playButtonColors = buttonColors(
        backgroundColor = MaterialTheme.colors.primary
    )

    val skipButtonColors = buttonColors(
        backgroundColor = MaterialTheme.colors.surface
    )

    var progressValue by remember {
        mutableStateOf(0f)
    }

    val progress by animateFloatAsState(targetValue = progressValue)

    var start by remember {
        mutableStateOf(false)
    }

    ConstraintLayout(
        modifier = Modifier.fillMaxWidth(),
        constraintSet = playerConstraints(20.dp)
    ) {
        ButtonWithIcon(
            modifier = Modifier.layoutId("playButton"),
            onClick = { start = !start },
            icon = Icons.Rounded.PlayArrow,
            shape = RoundedCornerShape(50),
            buttonColors = playButtonColors,
            contentDescription = "play"
        )

        ButtonWithIcon(
            modifier = Modifier.layoutId("skipNextButton"),
            onClick = { progressValue = progressValue.seekForward() },
            icon = Icons.Rounded.SkipNext,
            shape = CircleShape,
            buttonColors = skipButtonColors,
            contentDescription = "next"
        )

        ButtonWithIcon(
            modifier = Modifier.layoutId("skipPrevButton"),
            onClick = { progressValue = progressValue.seekBack() },
            icon = Icons.Rounded.SkipPrevious,
            shape = CircleShape,
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

@Preview
@Composable
fun PlayerPreview() {
    Player(
        songName = "Baila Conmigo",
        artistName = "Selena Gomez, Rauw Alejandro",
        albumArt = R.drawable.error_image
    )
}

fun Float.seekForward(by: Float = 0.1f): Float =
    if (this.plus(by) >= 1f) 1f
    else this.plus(by)

fun Float.seekBack(by: Float = 0.1f): Float =
    if (this.minus(by) <= 0f) 0f
    else this.minus(by)