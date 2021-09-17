package com.looker.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.looker.components.ComponentConstants.tweenAnimation

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MaterialCard(
    modifier: Modifier = Modifier,
    elevation: Dp = 0.dp,
    contentColor: Color = MaterialTheme.colors.onBackground,
    backgroundColor: Color = MaterialTheme.colors.background,
    rippleColor: Color = MaterialTheme.colors.primary,
    shape: Shape = MaterialTheme.shapes.medium,
    onClick: () -> Unit = {},
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    content: @Composable () -> Unit
) {
    val animateColor by animateColorAsState(
        targetValue = backgroundColor,
        animationSpec = tweenAnimation()
    )

    Card(
        modifier = modifier,
        elevation = elevation,
        shape = shape,
        backgroundColor = animateColor,
        contentColor = contentColor,
        indication = rememberRipple(color = rippleColor),
        interactionSource = interactionSource,
        content = content,
        onClick = onClick,
    )
}