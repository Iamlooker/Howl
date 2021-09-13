package com.looker.ui_player.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.looker.components.WrappedText
import com.looker.components.compositeOverBackground

@Composable
fun QueueHeader(
    modifier: Modifier = Modifier,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    openQueue: () -> Unit
) {
    Box(
        modifier = modifier,
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
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(CircleShape)
                    .background(MaterialTheme.colors.primaryVariant.compositeOverBackground())
                    .clickable(
                        onClick = openQueue,
                        indication = rememberRipple(
                            color = MaterialTheme.colors.secondary.copy(0.3f)
                        ),
                        interactionSource = interactionSource
                    )
                    .padding(8.dp),
                text = "Next Song Name",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.caption
            )
        }
    }
}