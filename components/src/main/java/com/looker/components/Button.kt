package com.looker.components

import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.looker.components.localComposers.LocalDurations

@Composable
fun ShapedIconButton(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    shape: CornerBasedShape = CircleShape,
    buttonColors: ButtonColors = ButtonDefaults.buttonColors(),
    buttonElevation: ButtonElevation = ButtonDefaults.elevation(0.dp, 6.dp),
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    contentDescription: String?,
    onClick: () -> Unit,
) {
    Button(
        modifier = modifier,
        onClick = onClick,
        shape = shape,
        colors = buttonColors,
        elevation = buttonElevation,
        contentPadding = contentPadding
    ) {
        Crossfade(
            targetState = icon,
            animationSpec = tweenAnimation(LocalDurations.current.crossFade)
        ) {
            Icon(
                imageVector = it,
                contentDescription = contentDescription
            )
        }
    }
}

@Composable
fun ToggleButton(
    modifier: Modifier = Modifier,
    toggled: Boolean,
    icon: ImageVector,
    shape: CornerBasedShape = CircleShape,
    activeColor: Color = MaterialTheme.colors.secondaryVariant,
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    onToggle: () -> Unit,
    contentDescription: String?
) {
    val toggleColor by animateColorAsState(
        targetValue = if (toggled) activeColor.compositeOverBackground()
        else MaterialTheme.colors.surface,
        animationSpec = tweenAnimation(LocalDurations.current.crossFade)
    )

    val toggleButtonColors = ButtonDefaults.buttonColors(backgroundColor = toggleColor)

    ShapedIconButton(
        modifier = modifier,
        icon = icon,
        shape = shape,
        buttonColors = toggleButtonColors,
        contentPadding = contentPadding,
        onClick = onToggle,
        contentDescription = contentDescription
    )
}