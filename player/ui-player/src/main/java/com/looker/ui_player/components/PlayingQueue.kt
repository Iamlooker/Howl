package com.looker.ui_player.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.looker.components.WrappedText
import com.looker.components.compositeOverBackground

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