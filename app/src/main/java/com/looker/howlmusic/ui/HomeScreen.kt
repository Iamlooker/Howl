package com.looker.howlmusic.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.looker.components.*
import com.looker.components.ext.backgroundGradient
import com.looker.components.localComposers.LocalDurations
import com.looker.components.state.SheetsState
import com.looker.constants.Resource
import com.looker.constants.states.ToggleState
import com.looker.domain_music.Album
import com.looker.domain_music.Song
import com.looker.howlmusic.HowlViewModel
import com.looker.howlmusic.ui.components.Backdrop
import com.looker.howlmusic.ui.components.BottomAppBar
import com.looker.howlmusic.ui.components.HomeNavGraph
import com.looker.howlmusic.ui.components.HomeScreens
import com.looker.howlmusic.utils.extension.isPlaying
import com.looker.howlmusic.utils.extension.toSong
import com.looker.ui_albums.AlbumsBottomSheetContent
import com.looker.ui_player.PlayerControls
import com.looker.ui_player.PlayerHeader
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Home(viewModel: HowlViewModel = viewModel()) {

	val scope = rememberCoroutineScope()
	val state = rememberBackdropScaffoldState(BackdropValue.Concealed)

	val currentSong by viewModel.nowPlaying.observeAsState()
	val backdropValue by viewModel.backdropValue.collectAsState()
	val enableGesture by viewModel.enableGesture.collectAsState()

	val songsList by viewModel.mediaItems.collectAsState()

	val playbackState by viewModel.playbackState.observeAsState()

	LaunchedEffect(state.isConcealed) {
		viewModel.setBackdropValue(state.currentValue)
	}

	Backdrop(
		modifier = Modifier,
		state = state,
		isPlaying = playbackState?.isPlaying == true,
		enableGesture = enableGesture,
		header = {

			val toggleIcon by viewModel.toggleIcon.collectAsState()
			val toggle by viewModel.toggle.collectAsState()
			val backgroundColor = rememberDominantColorState()

			LaunchedEffect(currentSong) {
				backgroundColor.updateColorsFromImageUrl(currentSong?.toSong?.albumArt)
			}

			LaunchedEffect(backdropValue, playbackState) {
				viewModel.setToggleIcon(playbackState?.isPlaying == true)
				viewModel.updateToggle()
			}

			val animatedBackgroundScrim by animateColorAsState(
				targetValue = backgroundColor.color.copy(0.3f),
				animationSpec = tweenAnimation(LocalDurations.current.crossFade)
			)

			Player(
				modifier = Modifier.backgroundGradient(animatedBackgroundScrim),
				icon = toggleIcon,
				currentSong = currentSong.toSong,
				isPlaying = playbackState?.isPlaying == true,
				toggled = toggle,
				toggleAction = { viewModel.onToggleClick() }
			)
		},
		frontLayerContent = {
			val bottomSheetState =
				rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)

			val albumsList by viewModel.albumsList.collectAsState()
			val currentAlbum by viewModel.currentAlbum.collectAsState()

			val albumDominant = rememberDominantColorState()

			LaunchedEffect(currentAlbum) {
				albumDominant.updateColorsFromImageUrl(currentAlbum.albumArt)
			}

			LaunchedEffect(bottomSheetState.isVisible, currentAlbum) {
				if (backdropValue == SheetsState.HIDDEN) viewModel.gestureState(!bottomSheetState.isVisible)
				else viewModel.gestureState(true)
			}

			FrontLayer(
				bottomSheetState = bottomSheetState,
				songsList = songsList,
				albumsList = albumsList,
				handleIcon = 1F,
				currentAlbum = currentAlbum,
				albumsDominantColor = albumDominant.color,
				onSongClick = { viewModel.onSongClick(it) },
				openPlayer = {
					scope.launch(Dispatchers.IO) {
						state.animateTo(BackdropValue.Revealed, myTween(400))
					}
				},
				onAlbumClick = {
					scope.launch {
						bottomSheetState.animateTo(
							ModalBottomSheetValue.HalfExpanded,
							myTween(400)
						)
					}
					viewModel.onAlbumClick(it)
				}
			)
		},
		backLayerContent = {

			val progress by viewModel.progress.collectAsState()
			val playIcon by viewModel.playIcon.collectAsState()

			Controls(
				isPlaying = playbackState?.isPlaying == true,
				progress = progress,
				playIcon = playIcon,
				onPlayPause = { currentSong?.toSong?.let { song -> viewModel.playMedia(song) } },
				skipNextClick = { viewModel.playNext() },
				skipPrevClick = { viewModel.playPrevious() },
				onSeek = { seekTo -> viewModel.onSeek(seekTo) },
			)
		}
	)
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun FrontLayer(
	bottomSheetState: ModalBottomSheetState,
	songsList: Resource<List<Song>>,
	albumsList: List<Album>,
	handleIcon: Float,
	currentAlbum: Album,
	albumsDominantColor: Color,
	openPlayer: () -> Unit,
	onSongClick: (Song) -> Unit,
	onAlbumClick: (Album) -> Unit,
) {
	val navController = rememberNavController()
	val items = listOf(HomeScreens.SONGS, HomeScreens.ALBUMS)
	BottomSheets(
		state = bottomSheetState,
		sheetContent = {
			AlbumsBottomSheetContent(
				currentAlbum = currentAlbum,
				songsList = songsList.data?.filter { it.mediaId == currentAlbum.albumId }
					?: emptyList(),
				dominantColor = albumsDominantColor.copy(0.4f)
			)
		}
	) {
		Scaffold(
			bottomBar = {
				BottomAppBar(
					modifier = Modifier.navigationBarsPadding(),
					navController = navController,
					items = items
				)
			},
			floatingActionButton = {
				ShapedIconButton(
					icon = Icons.Rounded.KeyboardArrowDown,
					backgroundColor = MaterialTheme.colors.primaryVariant.compositeOverBackground(),
					contentPadding = PaddingValues(vertical = 16.dp),
					contentDescription = "Expand Player",
					onClick = openPlayer
				)
			}
		) { bottomNavigationPadding ->
			Column(Modifier.padding(bottomNavigationPadding)) {
				HandleIcon(handleIcon) { openPlayer() }
				HomeNavGraph(
					navController = navController,
					songsList = songsList,
					albumsList = albumsList,
					onSongClick = onSongClick,
					onAlbumClick = onAlbumClick
				)
			}
		}
	}
}

@Composable
fun Player(
	modifier: Modifier = Modifier,
	currentSong: Song,
	isPlaying: Boolean,
	icon: ImageVector,
	toggled: ToggleState,
	toggleAction: () -> Unit,
) {
	PlayerHeader(
		modifier = modifier
			.statusBarsPadding()
			.padding(vertical = 20.dp),
		song = currentSong,
		isPlaying = isPlaying,
		toggleIcon = icon,
		toggled = toggled.enabled,
		toggleAction = toggleAction
	)
}

@Composable
fun Controls(
	modifier: Modifier = Modifier,
	progress: Float,
	isPlaying: Boolean,
	playIcon: ImageVector,
	onPlayPause: (Boolean) -> Unit,
	skipNextClick: () -> Unit,
	onSeek: (Float) -> Unit,
	skipPrevClick: () -> Unit,
) {
	Column(modifier) {
		PlayerControls(
			isPlaying = isPlaying,
			progressValue = progress,
			playIcon = playIcon,
			onPlayPause = { onPlayPause(it) },
			skipNextClick = skipNextClick,
			skipPrevClick = skipPrevClick,
			onSeek = { seekTo -> onSeek(seekTo) },
		)
		Spacer(Modifier.windowInsetsTopHeight(WindowInsets.statusBars))
	}
}