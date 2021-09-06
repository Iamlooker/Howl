package com.looker.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import com.looker.components.ComponentConstants.colorAnimationDuration
import com.looker.components.ComponentConstants.wallpaperSurfaceAlpha

@Composable
fun WallpaperTheme(
    dominantColorState: DominantColorState = rememberDominantColorState(),
    content: @Composable () -> Unit
) {

    val colors = MaterialTheme.colors.copy(
        surface = animateColorAsState(
            dominantColorState.color.copy(wallpaperSurfaceAlpha).compositeOverDayNight(),
            tween(colorAnimationDuration)
        ).value
    )
    MaterialTheme(colors = colors, content = content)
}