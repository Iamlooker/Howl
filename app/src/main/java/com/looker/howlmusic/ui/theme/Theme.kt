package com.looker.howlmusic.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun HowlMusicTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) {
        HowlColorsDark
    } else {
        HowlColorsLight
    }

    MaterialTheme(
        colors = colors,
        typography = HowlTypography,
        shapes = HowlShapes,
        content = content
    )
}