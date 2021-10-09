package com.looker.howlmusic.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import com.looker.components.localComposers.*
import com.looker.howlmusic.R

private val DefaultDuration = Durations()
private val DefaultElevation = Elevations()
private val AppIcon = Images(R.drawable.empty)

@Composable
fun HowlMusicTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) {
        HowlColorsDark
    } else {
        HowlColorsLight
    }

    CompositionLocalProvider(
        LocalDurations provides DefaultDuration,
        LocalElevations provides DefaultElevation,
        LocalImages provides AppIcon
    ) {
        MaterialTheme(
            colors = colors,
            typography = HowlTypography,
            shapes = HowlShapes,
            content = content
        )
    }
}