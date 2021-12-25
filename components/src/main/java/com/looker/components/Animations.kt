package com.looker.components

import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.TweenSpec
import androidx.compose.runtime.Composable
import com.looker.components.localComposers.LocalDurations

@Composable
fun <T> tweenAnimation(
	durationMillis: Int = LocalDurations.current.fadeIn,
	delayMillis: Int = 0,
	easing: Easing = FastOutSlowInEasing
) = myTween<T>(durationMillis, delayMillis, easing)

fun <T> myTween(
	durationMillis: Int,
	delayMillis: Int = 0,
	easing: Easing = FastOutSlowInEasing
) = TweenSpec<T>(durationMillis, delayMillis, easing)