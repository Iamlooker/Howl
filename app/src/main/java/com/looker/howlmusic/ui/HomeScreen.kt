package com.looker.howlmusic.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.RepeatOne
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.insets.navigationBarsHeight
import com.google.accompanist.insets.statusBarsHeight
import com.google.accompanist.insets.statusBarsPadding
import com.looker.components.*
import com.looker.components.ext.backgroundGradient
import com.looker.components.localComposers.LocalDurations
import com.looker.components.state.PlayState
import com.looker.components.state.SheetsState
import com.looker.constants.Resource
import com.looker.domain_music.Album
import com.looker.domain_music.Song
import com.looker.howlmusic.HowlViewModel
import com.looker.howlmusic.ui.components.Backdrop
import com.looker.howlmusic.ui.components.BottomAppBar
import com.looker.howlmusic.ui.components.HomeNavGraph
import com.looker.howlmusic.ui.components.HomeScreens
import com.looker.howlmusic.utils.extension.toSong
import com.looker.ui_albums.AlbumsBottomSheetContent
import com.looker.ui_player.PlayerControls
import com.looker.ui_player.PlayerHeader
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Home(viewModel: HowlViewModel) {

	val scope = rememberCoroutineScope()
	val state = rememberBackdropScaffoldState(BackdropValue.Concealed)

	val currentSong by viewModel.nowPlaying.observeAsState()
	val backdropValue by viewModel.backdropValue.collectAsState()
	val enableGesture by viewModel.enableGesture.collectAsState()

	val songsList by viewModel.mediaItems.collectAsState()

	val playState by viewModel.playState.collectAsState()

	LaunchedEffect(state.currentValue.name) {
		launch { viewModel.setBackdropValue(state.currentValue) }
	}

	Backdrop(
		modifier = Modifier,
		state = state,
		playState = playState,
		enableGesture = enableGesture,
		header = {

			val toggleIcon by viewModel.toggleIcon.collectAsState()
			val toggle by viewModel.toggle.collectAsState()
			val backgroundColor = rememberDominantColorState()
			val imageCorner = remember { mutableStateOf(50) }

			LaunchedEffect(currentSong) {
				launch {
					backgroundColor.updateColorsFromImageUrl(currentSong?.toSong?.albumArt)
				}
			}

			LaunchedEffect(playState) {
				launch(Dispatchers.IO) {
					imageCorner.value = when (playState) {
						PlayState.PLAYING -> 50
						PlayState.PAUSED -> 15
					}
				}
			}

			LaunchedEffect(backdropValue, playState) {
				launch {
					viewModel.setToggleIcon(backdropValue)
					viewModel.updateToggle()
				}
			}

			val corner by animateIntAsState(
				targetValue = imageCorner.value,
				animationSpec = tweenAnimation()
			)

			val animatedBackgroundScrim by animateColorAsState(
				targetValue = backgroundColor.color.copy(0.3f),
				animationSpec = tweenAnimation(LocalDurations.current.crossFade)
			)

			Player(
				modifier = Modifier.backgroundGradient(animatedBackgroundScrim),
				icon = toggleIcon,
				albumArt = currentSong?.toSong?.albumArt,
				songName = currentSong?.toSong?.songName,
				artistName = currentSong?.toSong?.artistName,
				toggled = toggle,
				imageCorner = corner,
				toggleAction = { }
			)
		},
		frontLayerContent = {
			val bottomSheetState =
				rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)

			val albumsList by viewModel.albumsList.collectAsState()
			val currentAlbum by viewModel.currentAlbum.collectAsState()

			val albumDominant = rememberDominantColorState()

			LaunchedEffect(currentAlbum) {
				launch { albumDominant.updateColorsFromImageUrl(currentAlbum.albumArt) }
			}

			LaunchedEffect(bottomSheetState.isVisible, currentAlbum) {
				launch {
					if (backdropValue == SheetsState.HIDDEN) {
						viewModel.gestureState(!bottomSheetState.isVisible)
					} else viewModel.gestureState(true)
				}
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

			Controls(
				isPlaying = playState,
				progress = progress,
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
	onAlbumClick: (Int) -> Unit,
) {
	val navController = rememberNavController()
	val items = listOf(HomeScreens.SONGS, HomeScreens.ALBUMS)
	BottomSheets(
		state = bottomSheetState,
		sheetContent = {
			AlbumsBottomSheetContent(
				currentAlbum = currentAlbum,
				songsList = songsList.data?.filter { it.albumId == currentAlbum.albumId }
					?: emptyList(),
				dominantColor = albumsDominantColor.copy(0.4f)
			)
		}
	) {
		Scaffold(
			bottomBar = {
				BottomAppBar(
					modifier = Modifier.navigationBarsHeight(56.dp),
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
	albumArt: String?,
	songName: String?,
	artistName: String?,
	icon: ImageVector,
	toggled: Boolean,
	imageCorner: Int,
	toggleAction: () -> Unit,
) {
	PlayerHeader(
		modifier = modifier
			.statusBarsPadding()
			.padding(bottom = 20.dp),
		songName = songName,
		artistName = artistName,
		albumArt = albumArt,
		onImageIcon = icon,
		repeatIcon = Icons.Rounded.RepeatOne,
		toggled = toggled,
		toggleAction = toggleAction,
		imageCorner = imageCorner
	)
}

@Composable
fun Controls(
	modifier: Modifier = Modifier,
	progress: Float,
	isPlaying: PlayState,
	onPlayPause: (PlayState) -> Unit,
	skipNextClick: () -> Unit,
	onSeek: (Float) -> Unit,
	skipPrevClick: () -> Unit,
) {
	Column(modifier) {
		PlayerControls(
			isPlaying = isPlaying,
			progressValue = progress,
			onPlayPause = { onPlayPause(it) },
			skipNextClick = skipNextClick,
			skipPrevClick = skipPrevClick,
			onSeek = { seekTo -> onSeek(seekTo) },
		)
		Spacer(Modifier.statusBarsHeight())
	}
}