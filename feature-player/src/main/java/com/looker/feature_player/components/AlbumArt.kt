package com.looker.feature_player.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun AlbumArt(
	modifier: Modifier = Modifier,
	onButtonClick: () -> Unit,
	backgroundColor: Color,
	contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
	buttonIcon: @Composable RowScope.() -> Unit,
	image: @Composable BoxScope.() -> Unit
) {
	Box(modifier = modifier.padding(horizontal = 20.dp)) {
		image()
		Button(
			modifier = Modifier
				.clip(MaterialTheme.shapes.medium)
				.align(Alignment.BottomEnd)
				.drawBehind { drawRect(backgroundColor) },
			elevation = ButtonDefaults.elevation(0.dp, 0.dp, 0.dp, 0.dp),
			contentPadding = contentPadding,
			colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent),
			onClick = onButtonClick,
			content = buttonIcon
		)
	}
}