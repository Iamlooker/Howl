package com.looker.components

import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween

object ComponentConstants {

    const val DefaultFadeInDuration = 400
    const val DefaultCrossFadeDuration = 500

    fun <T> tweenAnimation(
        durationMillis: Int = DefaultFadeInDuration,
        delayMillis: Int = 0,
        easing: Easing = FastOutSlowInEasing
    ) = tween<T>(durationMillis, delayMillis, easing)
}
