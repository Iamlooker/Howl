package com.looker.components

import android.graphics.Bitmap
import androidx.collection.LruCache
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import com.looker.components.ComponentConstants.colorAnimationDuration
import com.looker.components.ComponentConstants.wallpaperSurfaceAlpha

@Composable
fun WallpaperTheme(
    dominantColorState: DominantColorStateWallpaper = rememberWallpaperColor(),
    content: @Composable () -> Unit
) {

    val colors = MaterialTheme.colors.copy(
        surface = animateColorAsState(
            dominantColorState.color.copy(wallpaperSurfaceAlpha).compositeOverDayNight(),
            tween(colorAnimationDuration)
        ).value,
        primary = animateColorAsState(
            dominantColorState.onColor.compositeOver(dominantColorState.color),
            tween(colorAnimationDuration)
        ).value
    )
    MaterialTheme(colors = colors, content = content)
}

@Composable
fun rememberWallpaperColor(
    defaultColor: Color = MaterialTheme.colors.surface,
    defaultOnColor: Color = MaterialTheme.colors.onSurface,
    cacheSize: Int = 1,
): DominantColorStateWallpaper = remember {
    DominantColorStateWallpaper(defaultColor, defaultOnColor, cacheSize)
}

@Stable
class DominantColorStateWallpaper(
    private val defaultColor: Color,
    private val defaultOnColor: Color,
    cacheSize: Int = 1,
) {
    var color by mutableStateOf(defaultColor)
        private set
    var onColor by mutableStateOf(defaultOnColor)
        private set

    private val cache = when {
        cacheSize > 0 -> LruCache<Bitmap, DominantColors>(cacheSize)
        else -> null
    }

    fun updateColorsFromBitmap(bitmap: Bitmap?) {
        val result = bitmap?.let { calculateDominantColor(it) }
        color = result?.color ?: defaultColor
        onColor = result?.onColor ?: defaultOnColor
    }

    private fun calculateDominantColor(bitmap: Bitmap): DominantColors {
        return cache?.get(bitmap) ?: DominantColors(
            color = bitmap.getDominantColor() ?: defaultColor,
            onColor = bitmap.getVibrantColor() ?: defaultOnColor
        )
            .also { result -> cache?.put(bitmap, result) }

    }
}