package com.looker.components

import android.content.Context
import androidx.collection.LruCache
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.platform.LocalContext

fun Int.toColor() = Color(this)

@Composable
fun Color.compositeOverBackground(
    alpha: Float = 0.3f,
    backgroundColor: Color = MaterialTheme.colors.background
): Color =
    this.copy(alpha).compositeOver(backgroundColor)

@Composable
fun rememberDominantColorState(
    context: Context = LocalContext.current,
    defaultColor: Color = MaterialTheme.colors.surface,
    cacheSize: Int = 12,
): DominantColorState = remember {
    DominantColorState(context, defaultColor, cacheSize)
}

@Stable
class DominantColorState(
    private val context: Context,
    private val defaultColor: Color,
    cacheSize: Int = 50,
) {
    var color by mutableStateOf(defaultColor)
        private set

    private val cache = when {
        cacheSize > 0 -> LruCache<String, DominantColors>(cacheSize)
        else -> null
    }

    suspend fun updateColorsFromImageUrl(url: String?) {
        val result = calculateDominantColorFromUrl(url)
        color = result?.color ?: defaultColor
    }

    private suspend fun calculateDominantColorFromUrl(url: String?): DominantColors? {
        return url?.let {
            cache?.get(it)
                ?: calculateColorFromImageUrl(context, url)?.let { dominantColor ->
                    DominantColors(color = dominantColor).also { result -> cache?.put(url, result) }
                }
        }
    }
}

@Immutable
data class DominantColors(val color: Color = Color.Unspecified)

private suspend fun calculateColorFromImageUrl(
    context: Context,
    imageUrl: String,
): Color? {
    val bitmap = imageUrl.bitmap(context)

    val vibrant = bitmap.getVibrantColor()
    val dominant = bitmap.getDominantColor()

    return if (vibrant == Color(0)) dominant
    else vibrant
}
