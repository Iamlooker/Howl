package com.looker.ui_player.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Shuffle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.looker.components.ComponentConstants
import com.looker.components.ShapedIconButton
import com.looker.components.WrappedText
import com.looker.components.compositeOverBackground

@Composable
fun ShuffleButton(
    modifier: Modifier = Modifier,
    shuffle: Boolean,
    onShuffle: () -> Unit
) {
    val shuffleColor by animateColorAsState(
        targetValue = if (shuffle) MaterialTheme.colors.secondary.compositeOverBackground()
        else MaterialTheme.colors.surface, animationSpec = ComponentConstants.tweenAnimation()
    )

    val shuffleButtonColors = ButtonDefaults.buttonColors(backgroundColor = shuffleColor)

    ShapedIconButton(
        modifier = modifier.padding(20.dp),
        icon = Icons.Rounded.Shuffle,
        buttonColors = shuffleButtonColors,
        onClick = onShuffle,
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