package com.looker.components

import android.content.Context
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

object ComponentConstants {

    fun Context.calculateItemSize(height: Boolean, count: Int, padding: Dp = 0.dp): State<Dp> {
        val screenDensity = this.resources.displayMetrics.density
        val screenHeight = this.resources.displayMetrics.heightPixels
        val screenWidth = this.resources.displayMetrics.widthPixels
        val itemSizeInPx = if (height) {
            screenHeight / count
        } else {
            screenWidth / count
        }
        val itemSize = itemSizeInPx.dp / screenDensity

        return mutableStateOf(itemSize - padding)
    }

    const val DefaultFadeInDuration = 400
    const val DefaultCrossFadeDuration = 500

    fun <T> tweenAnimation(
        durationMillis: Int = DefaultFadeInDuration,
        delayMillis: Int = 0,
        easing: Easing = FastOutSlowInEasing
    ) = tween<T>(durationMillis, delayMillis, easing)
}
