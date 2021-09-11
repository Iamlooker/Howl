package com.looker.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
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
        Icon(
            imageVector = icon,
            contentDescription = contentDescription
        )
    }
}