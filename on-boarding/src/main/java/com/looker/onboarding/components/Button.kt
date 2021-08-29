package com.looker.onboarding.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.unit.dp

@Composable
fun ButtonWithIcon(
    buttonText: String,
    buttonIcon: ImageVector,
    buttonColor: Color,
    onClick: () -> Unit,
) {

    OutlinedButton(
        onClick = onClick
    ) {
        Row(
            modifier = Modifier.wrapContentSize(),
            horizontalArrangement = Arrangement.spacedBy(5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = buttonIcon,
                contentDescription = "Permission Icon",
                tint = buttonColor
            )
            Text(
                text = AnnotatedString(
                    text = buttonText,
                    SpanStyle(color = buttonColor)
                ),
                modifier = Modifier.animateContentSize()
            )
        }
    }
}