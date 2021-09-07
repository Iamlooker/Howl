package com.looker.ui_player

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults.buttonColors
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.SkipNext
import androidx.compose.material.icons.rounded.SkipPrevious
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
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
            IconButtonSet()
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

private fun decoupledConstraints(margin: Dp): ConstraintSet {
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
fun IconButtonSet() {

    val playButtonColors = buttonColors(
        backgroundColor = MaterialTheme.colors.primary
    )

    val skipButtonColors = buttonColors(
        backgroundColor = MaterialTheme.colors.surface
    )

    var default by remember {
        mutableStateOf(0f)
    }

    var start by remember {
        mutableStateOf(false)
    }

    val animate by animateFloatAsState(
        targetValue = default,
        animationSpec = spring()
    )

    ConstraintLayout(
        modifier = Modifier.fillMaxWidth(),
        constraintSet = decoupledConstraints(20.dp)
    ) {
        Button(
            modifier = Modifier.layoutId("playButton"),
            onClick = { start = !start },
            shape = RoundedCornerShape(50),
            colors = playButtonColors
        ) {
            Icon(
                imageVector = Icons.Rounded.PlayArrow,
                contentDescription = "play"
            )
        }
        Button(
            modifier = Modifier.layoutId("skipNextButton"),
            onClick = { default += 100f },
            shape = CircleShape,
            colors = skipButtonColors
        ) {
            Icon(
                imageVector = Icons.Rounded.SkipNext,
                contentDescription = "skipNext"
            )
        }
        Button(
            modifier = Modifier.layoutId("skipPrevButton"),
            onClick = { default -= 100f },
            shape = CircleShape,
            colors = skipButtonColors
        ) {
            Icon(
                imageVector = Icons.Rounded.SkipPrevious,
                contentDescription = "skipPrevButton"
            )
        }
        SeekbarCanvas(
            modifier = Modifier.layoutId("progressBar"),
            progress = animate
        )
    }
}

@Composable
fun SeekbarCanvas(
    modifier: Modifier = Modifier,
    progress: Float = 0f,
    backgroundColor: Color = MaterialTheme.colors.surface,
    primaryColor: Color = MaterialTheme.colors.primary,
    seekBarWidth: Float = 10f
) {
    BoxWithConstraints(modifier) {
        val boxScope = this
        val maxSeekLength = boxScope.constraints.maxWidth.toFloat()
        Canvas(modifier = Modifier.matchParentSize(), onDraw = {
            drawRoundRect(
                color = backgroundColor,
                size = Size(maxSeekLength, seekBarWidth),
                cornerRadius = CornerRadius(5f, 5f)
            )
            if (progress > 0 && progress < maxSeekLength) {
                drawCircle(
                    color = primaryColor,
                    radius = 10f,
                    center = Offset(progress, seekBarWidth / 2)
                )
                drawRoundRect(
                    color = primaryColor,
                    size = Size(progress, seekBarWidth),
                    cornerRadius = CornerRadius(5f, 5f)
                )
            }
        })
    }
}

@Preview
@Composable
fun PlayerPreview() {
//    SeekbarCanvas()
    Player(
        songName = "Baila Conmigo",
        artistName = "Selena Gomez, Rauw Alejandro",
        albumArt = R.drawable.error_image
    )
}

