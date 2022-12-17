package com.looker.core.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import com.looker.core.ui.localComposers.Elevations
import com.looker.core.ui.localComposers.LocalElevations

@Composable
fun HowlMusicTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
	val colors = if (darkTheme) darkColors else lightColors

	CompositionLocalProvider(LocalElevations provides Elevations()) {
		MaterialTheme(
			colors = colors,
			typography = HowlTypography,
			shapes = HowlShapes,
			content = content
		)
	}
}