package com.looker.feature_player.ui

import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.looker.components.AnimatedText
import com.looker.components.OpaqueIconButton
import com.looker.components.ext.backgroundGradient
import com.looker.components.localComposers.LocalDurations
import com.looker.components.overBackground
import com.looker.components.rememberDominantColorState
import com.looker.feature_player.ui.components.*
import com.looker.feature_player.utils.extension.toSong

@Composable
fun PlayerHeader(
	modifier: Modifier = Modifier,
	viewModel: PlayerViewModel = hiltViewModel(),
	toggleIcon: @Composable BoxScope.() -> Unit
) {
	val dominantColorState = rememberDominantColorState()
	val currentSong by viewModel.nowPlaying.collectAsState()
	val isPlaying by viewModel.isPlaying.collectAsState()
	Column(
		modifier = modifier
			.fillMaxWidth()
			.backgroundGradient(dominantColorState.color.overBackground())
			.statusBarsPadding(),
		horizontalAlignment = Alignment.CenterHorizontally,
		verticalArrangement = Arrangement.spacedBy(20.dp)
	) {
		LaunchedEffect(currentSong.toSong.albumArt) {
			dominantColorState.updateColorsFromImageUrl(currentSong.toSong.albumArt)
		}
		AlbumArt(
			modifier = Modifier
				.fillMaxWidth()
				.fillMaxHeight(0.27f),
			button = toggleIcon,
		) {
			val imageCorner by animateIntAsState(
				targetValue = if (isPlaying) 50 else 15,
				animationSpec = tween(LocalDurations.current.crossFade)
			)
			AsyncImage(
				modifier = Modifier
					.matchParentSize()
					.graphicsLayer {
						clip = true
						shape = RoundedCornerShape(imageCorner)
					},
				model = currentSong.toSong.albumArt,
				contentScale = ContentScale.Crop,
				contentDescription = "Album Art"
			)
		}
		SongText {
			AnimatedText(
				text = currentSong.toSong.name,
				maxLines = 2,
				style = MaterialTheme.typography.h4
			)
			AnimatedText(
				text = currentSong.toSong.artist,
				style = MaterialTheme.typography.subtitle1,
				fontWeight = FontWeight.SemiBold
			)
		}
	}
}

@Composable
fun Controls(
	modifier: Modifier = Modifier,
	viewModel: PlayerViewModel = hiltViewModel()
) {
	val isPlaying by viewModel.isPlaying.collectAsState()
	val currentSong by viewModel.nowPlaying.collectAsState()
	Column(
		modifier = modifier.padding(20.dp),
		verticalArrangement = Arrangement.spacedBy(20.dp)
	) {
		PlayAndSkipButton(
			skipNextClick = viewModel::playNext
		) {
			val buttonShape by animateIntAsState(
				targetValue = if (isPlaying) 50 else 15,
				animationSpec = tween(LocalDurations.current.crossFade)
			)
			OpaqueIconButton(
				modifier = Modifier
					.height(60.dp)
					.weight(3f)
					.graphicsLayer {
						clip = true
						shape = RoundedCornerShape(buttonShape)
					},
				onClick = { viewModel.playMedia(currentSong.toSong) },
				backgroundColor = MaterialTheme.colors.primaryVariant.overBackground(0.9f),
				contentColor = MaterialTheme.colors.onPrimary
			) {
				val playIcon by viewModel.playIcon.collectAsState()
				PlayPauseIcon(playIcon)
			}
		}
		PreviousAndSeekBar(
			skipPrevClick = viewModel::playPrevious
		) {
			val progress by viewModel.progress.collectAsState()
			SeekBar(
				modifier = Modifier.height(60.dp),
				progress = progress,
				onValueChange = { viewModel.onSeek(it) },
				onValueChanged = { viewModel.onSeeked() }
			)
		}
	}
}