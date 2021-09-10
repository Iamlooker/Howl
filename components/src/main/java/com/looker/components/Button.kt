package com.looker.components

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun ShapedIconButton(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    shape: CornerBasedShape = CircleShape,
    buttonColors: ButtonColors = ButtonDefaults.buttonColors(),
    buttonElevation: ButtonElevation = ButtonDefaults.elevation(0.dp, 0.dp),
    onClick: () -> Unit,
    contentDescription: String?
) {
    Button(
        modifier = modifier,
        onClick = onClick,
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