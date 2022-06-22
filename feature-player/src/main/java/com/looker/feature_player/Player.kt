package com.looker.feature_player

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateInt
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
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
import com.looker.core_common.states.SheetsState
import com.looker.core_service.utils.extension.toSong
import com.looker.core_ui.AnimatedText
import com.looker.core_ui.OpaqueIconButton
import com.looker.core_ui.ext.backgroundGradient
import com.looker.core_ui.overBackground
import com.looker.core_ui.rememberDominantColorState
import com.looker.feature_player.components.*
import com.looker.feature_player.queue.PlayerQueue
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest

@Composable
fun PlayerHeader(
	modifier: Modifier = Modifier,
	viewModel: PlayerViewModel = hiltViewModel(),
	onSheetStateChange: () -> StateFlow<SheetsState>
) {
	val dominantColorState = rememberDominantColorState()
	val isPlaying by viewModel.isPlaying.collectAsState()
	val currentSong by viewModel.nowPlaying.collectAsState()
	val toggleButtonState by viewModel.toggleStream.collectAsState()
	Column(
		modifier = modifier
			.fillMaxWidth()
			.backgroundGradient(dominantColorState.color.overBackground())
			.statusBarsPadding(),
		horizontalAlignment = Alignment.CenterHorizontally,
		verticalArrangement = Arrangement.spacedBy(20.dp)
	) {
		AlbumArt(
			modifier = Modifier
				.fillMaxWidth()
				.fillMaxHeight(0.27f),
			button = {
				LaunchedEffect(onSheetStateChange) {
					onSheetStateChange().collectLatest { viewModel.setBackdrop(it) }
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
					PlayPauseIcon(icon = toggleIcon)
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
			AsyncImage(
				modifier = Modifier
					.matchParentSize()
					.graphicsLayer {
						clip = true
						shape = RoundedCornerShape(imageCorner)
						scaleX = scale
						scaleY = scale
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
				onClick = { viewModel.playMedia() },
				backgroundColor = MaterialTheme.colors.primaryVariant.overBackground(0.9f),
				contentColor = MaterialTheme.colors.onPrimary,
				shape = RoundedCornerShape(15)
			) {
				PlayPauseIcon(playIcon)
			}
		}
		PreviousAndSeekBar(skipPrevClick = viewModel::playPrevious) {
			SeekBar(
				modifier = Modifier.height(60.dp),
				progress = progress,
				onValueChange = viewModel::onSeek,
				onValueChanged = viewModel::onSeeked
			)
		}
	}
}