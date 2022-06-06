package com.looker.feature_album.sheet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.looker.core_model.Album

@Composable
fun DetailSheetContent(
	album: Album,
	modifier: Modifier = Modifier,
	songsList: LazyListScope.() -> Unit,
	albumArt: @Composable BoxScope.() -> Unit
) {
	Column(modifier.padding(16.dp)) {
		AlbumsDetailsItem(album = album, albumArt = albumArt)
		LazyColumn(
			modifier = Modifier
				.padding(16.dp)
				.clip(MaterialTheme.shapes.medium)
				.background(MaterialTheme.colors.background),
			content = songsList
		)
		Spacer(modifier = Modifier.height(24.dp))
	}
}