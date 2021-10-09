package com.looker.components.localComposers

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf

@Immutable
data class Durations(val fadeIn: Int = 400, val crossFade: Int = 500)

val LocalDurations = staticCompositionLocalOf { Durations() }