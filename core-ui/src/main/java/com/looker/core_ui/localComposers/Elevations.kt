package com.looker.core_ui.localComposers

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Immutable
data class Elevations(
	val default: Dp = 0.dp,
	val low: Dp = 4.dp,
	val high: Dp = 12.dp,
)

val LocalElevations = staticCompositionLocalOf { Elevations() }