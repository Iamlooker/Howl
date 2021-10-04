package com.looker.components

import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.looker.components.ComponentConstants.tweenAnimation

@Composable
fun ShapedIconButton(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    shape: CornerBasedShape = CircleShape,
    buttonColors: ButtonColors = ButtonDefaults.buttonColors(),
    buttonElevation: ButtonElevation = ButtonDefaults.elevation(0.dp, 6.dp),
    contentDescription: String?,
    onClick: () -> Unit,
) {
    Button(
        modifier = modifier,
        onClick = onClick,
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
    activeColor: Color = MaterialTheme.colors.secondaryVariant,
    onToggle: () -> Unit,
    contentDescription: String?
) {
    val toggleColor by animateColorAsState(
        targetValue = if (toggled) activeColor.compositeOverBackground()
        else MaterialTheme.colors.surface, animationSpec = tweenAnimation()
    )

    val toggleButtonColors = ButtonDefaults.buttonColors(backgroundColor = toggleColor)

    ShapedIconButton(
        modifier = modifier.padding(20.dp),
        icon = icon,
        shape = shape,
        buttonColors = toggleButtonColors,
        onClick = onToggle,
        contentDescription = contentDescription
    )
}