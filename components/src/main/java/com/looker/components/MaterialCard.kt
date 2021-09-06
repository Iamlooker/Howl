package com.looker.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.TweenSpec
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MaterialCard(
    modifier: Modifier = Modifier,
    elevation: Dp = 3.dp,
    contentColor: Color = MaterialTheme.colors.onBackground,
    backgroundColor: Color = MaterialTheme.colors.background,
    rippleColor: Color = MaterialTheme.colors.primary,
    shape: Shape = MaterialTheme.shapes.medium,
    onClick: () -> Unit = {},
    content: @Composable () -> Unit
) {

    val animateColor by animateColorAsState(
        targetValue = backgroundColor,
        animationSpec = TweenSpec(500)
    )

    Card(
        modifier = modifier,
        shape = shape,
        backgroundColor = animateColor,
        contentColor = contentColor,
        interactionSource = MutableInteractionSource(),
        indication = rememberRipple(color = rippleColor),
        elevation = elevation,
        onClick = onClick,
        content = content,
    )
}