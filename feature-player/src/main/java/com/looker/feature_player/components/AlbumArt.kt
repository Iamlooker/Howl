package com.looker.feature_player.components

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AlbumArt(
	modifier: Modifier = Modifier,
	button: @Composable BoxScope.() -> Unit,
	image: @Composable BoxScope.() -> Unit
) {
	Box(modifier = modifier.padding(horizontal = 20.dp)) {
		image()
		button()
	}
}