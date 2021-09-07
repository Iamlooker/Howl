package com.looker.components

import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonColors
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun ButtonWithIcon(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    shape: CornerBasedShape,
    buttonColors: ButtonColors = ButtonDefaults.buttonColors(),
    onClick: () -> Unit,
    contentDescription: String?
) {
    Button(
        modifier = modifier,
        onClick = onClick,
        shape = shape,
        colors = buttonColors
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription
        )
    }
}