package com.looker.components

import android.annotation.SuppressLint
import android.graphics.Bitmap
import androidx.collection.LruCache
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.palette.graphics.Palette
import com.looker.components.ComponentConstants.colorAnimationDuration
import com.looker.components.ComponentConstants.wallpaperSurfaceAlpha

@SuppressLint("MissingPermission")
@Composable
fun WallpaperTheme(
    dominantColorState: DominantColorStateWallpaper = rememberWallpaperColor(),
    content: @Composable () -> Unit
) {

    val colors = MaterialTheme.colors.copy(
        surface = animateColorAsState(
            dominantColorState.color,
            tween(colorAnimationDuration)
        ).value,
        onSurface = animateColorAsState(
            dominantColorState.onColor,
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

    fun updateColorsFromBitmap(bitmap: Bitmap) {
        val result = calculateDominantColor(bitmap)
        color = result?.color ?: defaultColor
        onColor = result?.onColor ?: defaultOnColor
    }

    private fun calculateDominantColor(bitmap: Bitmap): DominantColors? {
        return cache?.get(bitmap) ?: calculateColorFromBitmap(bitmap)?.let { dominantColor ->
            DominantColors(
                color = dominantColor.copy(wallpaperSurfaceAlpha),
                onColor = dominantColor
            )
                .also { result -> cache?.put(bitmap, result) }
        }
    }
}

private fun calculateColorFromBitmap(
    bitmap: Bitmap?,
    getDominant: Boolean = true,
): Color? {

    val swatch = bitmap?.let {
        Palette.Builder(it)
            .resizeBitmapArea(0)
            .clearFilters()
            .maximumColorCount(8)
            .generate()
    }

    val vibrant = swatch?.getVibrantColor(0)
    val dominant = swatch?.getDominantColor(0)

    return if (getDominant) dominant?.let { Color(it) }
    else vibrant?.let { Color(it) }
}