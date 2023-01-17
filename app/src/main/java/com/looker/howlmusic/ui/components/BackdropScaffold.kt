package com.looker.howlmusic.ui.components

import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp

@ExperimentalMaterialApi
@Composable
fun Backdrop(
	modifier: Modifier = Modifier,
	state: BackdropScaffoldState,
	peekHeight: @Composable () -> Dp = { BackdropScaffoldDefaults.PeekHeight },
	header: @Composable () -> Unit,
	backLayerContent: @Composable () -> Unit,
	frontLayerContent: @Composable () -> Unit
) {
	BackdropScaffold(
		modifier = modifier,
		scaffoldState = state,
		appBar = header,
		peekHeight = peekHeight(),
		backLayerContent = backLayerContent,
		frontLayerContent = frontLayerContent,
		backLayerBackgroundColor = MaterialTheme.colors.background,
		frontLayerShape = MaterialTheme.shapes.large,
		frontLayerBackgroundColor = MaterialTheme.colors.background,
	)
}