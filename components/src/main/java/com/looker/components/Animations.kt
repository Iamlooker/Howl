package com.looker.components

import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import com.looker.components.localComposers.LocalDurations

@Composable
fun <T> tweenAnimation(
    durationMillis: Int = LocalDurations.current.fadeIn,
    delayMillis: Int = 0,
    easing: Easing = FastOutSlowInEasing
) = tween<T>(durationMillis, delayMillis, easing)