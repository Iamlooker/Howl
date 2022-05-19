package com.looker.components.localComposers

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf

@Immutable
data class Durations(val fast: Int = 100, val fadeIn: Int = 350, val crossFade: Int = 500, val slow: Int = 750)
val LocalDurations = staticCompositionLocalOf { Durations() }