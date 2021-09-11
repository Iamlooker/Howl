package com.looker.howlmusic.ui.theme

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import com.looker.components.ComponentConstants.tweenAnimation
import com.looker.components.ComponentConstants.wallpaperSurfaceAlpha
import com.looker.components.DominantColorState
import com.looker.components.compositeOverBackground
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
            dominantColorState.color.compositeOverBackground(wallpaperSurfaceAlpha),
            tweenAnimation()
        ).value
    )
    MaterialTheme(colors = colors, content = content)
}