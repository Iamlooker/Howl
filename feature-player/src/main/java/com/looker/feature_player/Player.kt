package com.looker.feature_player

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
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
import com.looker.components.state.SheetsState
import com.looker.core_service.utils.extension.toSong
import com.looker.feature_player.components.AlbumArt
import com.looker.feature_player.components.PlayAndSkipButton
import com.looker.feature_player.components.PlayPauseIcon
import com.looker.feature_player.components.PreviousAndSeekBar
import com.looker.feature_player.components.SeekBar
import com.looker.feature_player.components.SongText

@Composable
fun PlayerHeader(
	modifier: Modifier = Modifier,
	viewModel: PlayerViewModel = hiltViewModel(),
	onSheetStateChange: @Composable () -> SheetsState
) {
	val dominantColorState = rememberDominantColorState()
	Column(
		modifier = modifier
			.fillMaxWidth()
			.backgroundGradient(dominantColorState.color.overBackground())
			.statusBarsPadding(),
		horizontalAlignment = Alignment.CenterHorizontally,
		verticalArrangement = Arrangement.spacedBy(20.dp)
	) {
		val currentSong by viewModel.nowPlaying.collectAsState()
		AlbumArt(
			modifier = Modifier
				.fillMaxWidth()
				.fillMaxHeight(0.27f),
			button = {
				viewModel.setBackdrop(onSheetStateChange())
				val toggleButtonState by viewModel.toggleStream.collectAsState()
				val toggleColor by animateColorAsState(
					targetValue =
					if (toggleButtonState.enabled) MaterialTheme.colors.secondaryVariant.overBackground()
					else MaterialTheme.colors.background,
					animationSpec = tween(LocalDurations.current.crossFade)
				)
				Button(
					modifier = Modifier
						.clip(MaterialTheme.shapes.medium)
						.align(Alignment.BottomEnd)
						.drawBehind { drawRect(toggleColor) },
					colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent),
					elevation = ButtonDefaults.elevation(0.dp, 0.dp, 0.dp, 0.dp),
					onClick = { viewModel.onToggleClick(toggleButtonState.toggleState) },
				) {
					val toggleIcon by remember(toggleButtonState) { mutableStateOf(toggleButtonState.icon) }
					PlayPauseIcon(icon = toggleIcon)
				}
			},
		) {
			LaunchedEffect(currentSong.toSong.albumArt) {
				dominantColorState.updateColorsFromImageUrl(currentSong.toSong.albumArt)
			}
			val isPlaying by viewModel.isPlaying.collectAsState()
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
	Column(
		modifier = modifier.padding(20.dp),
		verticalArrangement = Arrangement.spacedBy(20.dp)
	) {
		val isPlaying by viewModel.isPlaying.collectAsState()
		PlayAndSkipButton(skipNextClick = viewModel::playNext) {
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
				onClick = { viewModel.playMedia() },
				backgroundColor = MaterialTheme.colors.primaryVariant.overBackground(0.9f),
				contentColor = MaterialTheme.colors.onPrimary,
				shape = RoundedCornerShape(15)
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
				onValueChange = viewModel::onSeek,
				onValueChanged = viewModel::onSeeked
			)
		}
	}
}