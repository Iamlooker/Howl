package com.looker.core.player

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateInt
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.looker.core.common.states.SheetsState
import com.looker.core.player.components.*
import com.looker.core.player.queue.PlayerQueue
import com.looker.core.service.extensions.toSong
import com.looker.core.ui.components.*
import com.looker.core.ui.ext.backgroundGradient

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun PlayerHeader(
	modifier: Modifier = Modifier,
	viewModel: PlayerViewModel = hiltViewModel(),
	onSheetStateChange: () -> SheetsState = { SheetsState.HIDDEN }
) {
	val dominantColorState = rememberDominantColorState()
	val isPlaying by viewModel.isPlaying.collectAsState()
	val currentSong by viewModel.nowPlaying.collectAsState()
	val toggleButtonState by viewModel.toggleStream.collectAsState()
	Column(
		modifier = modifier
			.fillMaxWidth()
			.backgroundGradient { dominantColorState.color.overBackground() }
			.statusBarsPadding(),
		horizontalAlignment = Alignment.CenterHorizontally,
		verticalArrangement = Arrangement.spacedBy(20.dp)
	) {
		AlbumArt(
			modifier = Modifier
				.width(450.dp)
				.height(250.dp),
			button = {
				LaunchedEffect(onSheetStateChange()) {
					viewModel.setBackdrop(onSheetStateChange())
				}
				val toggleColor by animateColorAsState(
					targetValue =
					if (toggleButtonState.enabled) MaterialTheme.colors.secondaryVariant
						.overBackground(0.9f)
					else MaterialTheme.colors.background
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
					PlayPauseIcon(
						tint = {
							if (toggleButtonState.enabled) MaterialTheme.colors.onSecondary
							else MaterialTheme.colors.onBackground
						},
						icon = { toggleIcon }
					)
				}
			},
		) {
			LaunchedEffect(currentSong.toSong.albumArt) {
				dominantColorState.updateColorsFromImageUrl(currentSong.toSong.albumArt)
			}
			val transition = updateTransition(
				targetState = isPlaying,
				label = "Album Art Transition"
			)
			val imageCorner by transition.animateInt(label = "Corner Size") {
				if (it) 50 else 15
			}
			val scale by transition.animateFloat(label = "Scale") {
				if (it) 1f else 0.95f
			}
			AnimatedContent(
				modifier = Modifier.matchParentSize(),
				targetState = currentSong.toSong.albumArt
			) {
				HowlImage(
					modifier = Modifier.graphicsLayer {
						clip = true
						shape = RoundedCornerShape(imageCorner)
						scaleX = scale
						scaleY = scale
					},
					data = { it },
					contentScale = ContentScale.Crop
				)
			}
		}
		SongText {
			/* WaterMark(text = currentSong.toSong.name) */
			AnimatedText(
				text = currentSong.toSong.name,
				style = MaterialTheme.typography.h2,
				maxLines = 2
			)
			AnimatedText(
				text = currentSong.toSong.artist,
				style = MaterialTheme.typography.h4,
				textColor = LocalContentColor.current.copy(0.7f)
			)
		}
		PlayerQueue()
	}
}

@Composable
fun Controls(
	modifier: Modifier = Modifier,
	viewModel: PlayerViewModel = hiltViewModel()
) {
	val isPlaying by viewModel.isPlaying.collectAsState()
	val playIcon by viewModel.playIcon.collectAsState()
	val progress by viewModel.progress.collectAsState()
	Column(
		modifier = modifier.padding(20.dp),
		verticalArrangement = Arrangement.spacedBy(20.dp)
	) {
			SeekBar(
				modifier = Modifier.height(60.dp),
				progress = { progress },
				onValueChange = viewModel::onSeek,
				onValueChanged = viewModel::onSeeked
			)
		PlayAndSkipButton(skipNextClick = viewModel::playNext) {
			val buttonShape by animateIntAsState(targetValue = if (isPlaying) 50 else 15)
			OpaqueIconButton(
				modifier = Modifier
					.height(60.dp)
					.weight(3f)
					.graphicsLayer {
						clip = true
						shape = RoundedCornerShape(buttonShape)
					},
				onClick = viewModel::playMedia,
				backgroundColor = MaterialTheme.colors.primaryVariant.overBackground(0.9f),
				contentColor = MaterialTheme.colors.onPrimary,
				shape = RectangleShape
			) {
				PlayPauseIcon { playIcon }
			}
		}
	}
}