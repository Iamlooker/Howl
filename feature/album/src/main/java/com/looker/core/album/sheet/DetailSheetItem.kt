package com.looker.core.album.sheet

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun DetailSheetContent(
	modifier: Modifier = Modifier,
	songsList: @Composable () -> Unit,
	albumText: @Composable () -> Unit,
	preference: @Composable () -> Unit,
	albumArt: @Composable BoxScope.() -> Unit
) {
	val scrollState = rememberScrollState()
	Column(
		modifier
			.padding(horizontal = 16.dp)
			.verticalScroll(scrollState)
	) {
		Spacer(modifier = Modifier.height(16.dp))
		AlbumsDetailsItem(albumText = albumText, albumArt = albumArt)
		preference()
		songsList()
		Spacer(modifier = Modifier.height(24.dp))
	}
}