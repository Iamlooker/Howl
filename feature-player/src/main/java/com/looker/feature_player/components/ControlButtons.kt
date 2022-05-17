package com.looker.feature_player.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.SkipNext
import androidx.compose.material.icons.rounded.SkipPrevious
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.looker.components.ShapedIconButton
import com.looker.components.overBackground


@Composable
fun PlayAndSkipButton(
	modifier: Modifier = Modifier,
	skipNextClick: () -> Unit,
	playButton: @Composable RowScope.() -> Unit
) {
	Row(
		modifier = modifier.fillMaxWidth(),
		horizontalArrangement = Arrangement.spacedBy(20.dp)
	) {
		playButton()
		ShapedIconButton(
			modifier = Modifier
				.height(60.dp)
				.weight(1f)
				.clip(CircleShape),
			onClick = skipNextClick,
			backgroundColor = MaterialTheme.colors.secondaryVariant.overBackground(0.9f),
			contentColor = MaterialTheme.colors.onSecondary
		) {
			Icon(
				imageVector = Icons.Rounded.SkipNext,
				contentDescription = null
			)
		}
	}
}

@Composable
fun PreviousAndSeekBar(
	modifier: Modifier = Modifier,
	skipPrevClick: () -> Unit,
	progressBar: @Composable () -> Unit
) {
	Row(
		modifier = modifier.fillMaxWidth(),
		horizontalArrangement = Arrangement.spacedBy(20.dp)
	) {
		ShapedIconButton(
			modifier = Modifier
				.height(60.dp)
				.weight(1f)
				.clip(CircleShape),
			onClick = skipPrevClick,
			backgroundColor = MaterialTheme.colors.secondaryVariant.overBackground(0.9f),
			contentColor = MaterialTheme.colors.onSecondary
		) {
			Icon(
				imageVector = Icons.Rounded.SkipPrevious,
				contentDescription = null
			)
		}
		Box(modifier = Modifier.weight(3f)) {
			progressBar()
		}
	}
}