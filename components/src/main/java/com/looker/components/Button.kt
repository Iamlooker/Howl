package com.looker.components

import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun ShapedIconButton(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    shape: CornerBasedShape = CircleShape,
    buttonColors: ButtonColors = ButtonDefaults.buttonColors(),
    buttonElevation: ButtonElevation = ButtonDefaults.elevation(0.dp, 0.dp),
    contentDescription: String?,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    onClick: () -> Unit,
) {
    val isPressed by interactionSource.collectIsPressedAsState()

    val animatedScale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = spring()
    )

    Button(
        modifier = modifier.scale(animatedScale),
        onClick = onClick,
        interactionSource = interactionSource,
        shape = shape,
        colors = buttonColors,
        elevation = buttonElevation
    ) {
        Crossfade(targetState = icon) {
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
    activeColor: Color = MaterialTheme.colors.secondary,
    onToggle: () -> Unit,
    contentDescription: String?
) {
    val shuffleColor by animateColorAsState(
        targetValue = if (toggled) activeColor.compositeOverBackground()
        else MaterialTheme.colors.surface, animationSpec = ComponentConstants.tweenAnimation()
    )

    val toggleButtonColors = ButtonDefaults.buttonColors(backgroundColor = shuffleColor)

    ShapedIconButton(
        modifier = modifier.padding(20.dp),
        icon = icon,
        shape = shape,
        buttonColors = toggleButtonColors,
        onClick = onToggle,
        contentDescription = contentDescription
    )
}