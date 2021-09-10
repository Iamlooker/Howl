package com.looker.components

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow

@Composable
fun WrappedText(
    modifier: Modifier = Modifier,
    text: String?,
    textColor: Color = Color.Unspecified,
    style: TextStyle = MaterialTheme.typography.body1,
    textAlign: TextAlign = TextAlign.Start,
) {
    Text(
        modifier = modifier,
        text = text ?: "No Title",
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        style = style,
        color = textColor,
        textAlign = textAlign
    )
}