package com.looker.howlmusic.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import com.looker.components.localComposers.Durations
import com.looker.components.localComposers.Elevations
import com.looker.components.localComposers.LocalDurations
import com.looker.components.localComposers.LocalElevations

@Composable
fun HowlMusicTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
	val colors = if (darkTheme) darkColors
	else lightColors

	CompositionLocalProvider(
		LocalDurations provides Durations(),
		LocalElevations provides Elevations()
	) {
		MaterialTheme(
			colors = colors,
			typography = HowlTypography,
			shapes = HowlShapes,
			content = content
		)
	}
}