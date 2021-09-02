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

@SuppressLint("MissingPermission")
@Composable
fun WallpaperTheme(
    dominantColorState: DominantColorStateWallpaper = rememberWallpaperColor(),
    content: @Composable () -> Unit
) {

    val colors = MaterialTheme.colors.copy(
        surface = animateColorAsState(
            dominantColorState.color.copy(0.2f),
            tween(1000)
        ).value,
        onSurface = animateColorAsState(
            dominantColorState.onColor,
            tween(1000)
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
                color = dominantColor,
                onColor = dominantColor.copy(alpha = 0.4f)
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