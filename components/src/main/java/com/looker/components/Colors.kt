package com.looker.components

import android.content.Context
import android.graphics.Bitmap
import androidx.collection.LruCache
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.core.graphics.drawable.toBitmap
import coil.Coil
import coil.request.ImageRequest
import coil.request.SuccessResult

fun Int.toColor() = Color(this)

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

    private val cacheBitmap = when {
        cacheSize > 0 -> LruCache<Bitmap, DominantColors>(cacheSize)
        else -> null
    }

    suspend fun updateColorsFromImageUrl(url: String) {
        val result = calculateDominantColorFromUrl(url)
        color = result?.color ?: defaultColor
    }

    fun updateColorsFromBitmap(bitmap: Bitmap?) {
        val result = bitmap?.let { calculateDominantColorFromBitmap(it) }
        color = result?.color ?: defaultColor
    }

    private suspend fun calculateDominantColorFromUrl(url: String): DominantColors? {
        return cache?.get(url) ?: calculateColorFromImageUrl(context, url)?.let { dominantColor ->
            DominantColors(color = dominantColor).also { result -> cache?.put(url, result) }
        }
    }

    private fun calculateDominantColorFromBitmap(bitmap: Bitmap): DominantColors {
        return cacheBitmap?.get(bitmap) ?: DominantColors(
            color = bitmap.getDominantColor() ?: defaultColor
        )
            .also { result -> cacheBitmap?.put(bitmap, result) }
    }
}

@Immutable
data class DominantColors(
    val color: Color = Color.Unspecified,
    val onColor: Color = Color.Unspecified
)


private suspend fun calculateColorFromImageUrl(
    context: Context,
    imageUrl: String,
): Color? {
    val r = ImageRequest.Builder(context)
        .data(imageUrl)
        .size(128).scale(coil.size.Scale.FILL)
        .allowHardware(false)
        .build()

    val bitmap = when (val result = Coil.execute(r)) {
        is SuccessResult -> result.drawable.toBitmap()
        else -> null
    }

    val vibrant = bitmap.getVibrantColor()
    val dominant = bitmap.getDominantColor()

    return if (vibrant == Color(0)) dominant
    else vibrant
}