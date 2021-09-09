package com.looker.howlmusic.ui.theme

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.compositeOver
import com.looker.components.ComponentConstants.colorAnimationDuration
import com.looker.components.ComponentConstants.wallpaperSurfaceAlpha
import com.looker.components.DominantColorState
import com.looker.components.rememberDominantColorState

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

@Composable
fun WallpaperTheme(
    dominantColorState: DominantColorState = rememberDominantColorState(),
    content: @Composable () -> Unit
) {
    val colors = MaterialTheme.colors.copy(
        surface = animateColorAsState(
            dominantColorState.color.copy(wallpaperSurfaceAlpha)
                .compositeOver(MaterialTheme.colors.background),
            tween(colorAnimationDuration)
        ).value
    )
    MaterialTheme(colors = colors, content = content)
}