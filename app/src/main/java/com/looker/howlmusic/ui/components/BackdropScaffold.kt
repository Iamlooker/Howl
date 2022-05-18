package com.looker.howlmusic.ui.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.material.BackdropScaffold
import androidx.compose.material.BackdropScaffoldState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp

@ExperimentalMaterialApi
@Composable
fun Backdrop(
	modifier: Modifier = Modifier,
	state: BackdropScaffoldState,
	isPlaying: Boolean = false,
	header: @Composable () -> Unit,
	backLayerContent: @Composable () -> Unit,
	frontLayerContent: @Composable () -> Unit
) {
	val expandedPeekHeight = with(LocalConfiguration.current) { screenHeightDp.dp / 3 }
	val peekHeight by animateDpAsState(targetValue = if (isPlaying) expandedPeekHeight else 56.dp)
	BackdropScaffold(
		modifier = modifier,
		scaffoldState = state,
		appBar = header,
		backLayerContent = backLayerContent,
		frontLayerContent = frontLayerContent,
		backLayerBackgroundColor = MaterialTheme.colors.background,
		peekHeight = peekHeight,
		frontLayerShape = MaterialTheme.shapes.large,
		frontLayerBackgroundColor = MaterialTheme.colors.background,
	)
}