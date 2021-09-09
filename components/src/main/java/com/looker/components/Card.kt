package com.looker.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MaterialCard(
    modifier: Modifier = Modifier,
    elevation: Dp = 3.dp,
    contentColor: Color = MaterialTheme.colors.onBackground,
    backgroundColor: Color = MaterialTheme.colors.background,
    rippleColor: Color = MaterialTheme.colors.primary,
    shape: Shape = MaterialTheme.shapes.medium,
    onClick: () -> Unit = {},
    onLongClick: () -> Unit = {},
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    content: @Composable () -> Unit
) {

    val animateColor by animateColorAsState(
        targetValue = backgroundColor,
        animationSpec = tween(500)
    )

    Card(
        modifier = modifier
            .clip(shape)
            .combinedClickable(
                onClick = onClick,
                interactionSource = interactionSource,
                indication = rememberRipple(color = rippleColor),
                onLongClick = onLongClick
            ),
        backgroundColor = animateColor,
        contentColor = contentColor,
        elevation = elevation,
        content = content,
    )
}