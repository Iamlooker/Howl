package com.looker.howlmusic.ui

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.looker.components.*
import com.looker.components.localComposers.LocalDurations
import com.looker.constants.Resource
import com.looker.core_model.Album
import com.looker.core_model.Song
import com.looker.feature_player.PlayerHeader
import com.looker.howlmusic.ui.components.Backdrop
import com.looker.howlmusic.ui.components.BottomAppBar
import com.looker.howlmusic.ui.components.HomeNavGraph
import com.looker.howlmusic.ui.components.HomeScreens
import com.looker.howlmusic.utils.extension.isPlaying
import com.looker.howlmusic.utils.extension.toSong
import com.looker.ui_albums.AlbumsBottomSheetContent
import com.looker.ui_player.PlayerControls
import com.looker.ui_player.components.SeekBar
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Home(viewModel: HowlViewModel = viewModel()) {

	val scope = rememberCoroutineScope()
	val state = rememberBackdropScaffoldState(BackdropValue.Concealed)
	val currentSong by viewModel.nowPlaying.collectAsState()
	val playbackState by viewModel.playbackState.collectAsState()

	Backdrop(
		modifier = Modifier,
		state = state,
		header = {
			// TODO: Add Background Scrim
			PlayerHeader(
				modifier = Modifier.statusBarsPadding(),
				onToggleClick = { viewModel.onToggleClick() },
				songText = {
					Text(
						modifier = Modifier.animateContentSize(),
						text = currentSong.toSong.name,
						style = MaterialTheme.typography.h4,
						maxLines = 2,
						overflow = TextOverflow.Ellipsis,
						textAlign = TextAlign.Center
					)
					Text(
						modifier = Modifier.animateContentSize(),
						text = currentSong.toSong.artist,
						style = MaterialTheme.typography.subtitle1,
						fontWeight = FontWeight.SemiBold,
						maxLines = 1,
						overflow = TextOverflow.Ellipsis,
						textAlign = TextAlign.Center
					)
				},
				toggleIcon = {
					val toggleIcon by viewModel.toggleIcon.collectAsState()
					Icon(imageVector = toggleIcon, contentDescription = null)
				}
			) {
				val imageCorner by animateIntAsState(
					targetValue = if (playbackState.isPlaying) 50 else 15,
					animationSpec = tweenAnimation(LocalDurations.current.crossFade)
				)
				AsyncImage(
					modifier = Modifier
						.matchParentSize()
						.graphicsLayer {
							shape = RoundedCornerShape(imageCorner)
							clip = true
						},
					model = currentSong.toSong.albumArt,
					contentScale = ContentScale.Crop,
					contentDescription = "Album Art"
				)
			}
		},
		frontLayerContent = {
			val bottomSheetState =
				rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)

			val albumsList by viewModel.albumsList.collectAsState()
			val songsList by viewModel.songsList.collectAsState()
			val currentAlbum by viewModel.currentAlbum.collectAsState()

			val albumDominant = rememberDominantColorState()

			LaunchedEffect(currentAlbum) {
				albumDominant.updateColorsFromImageUrl(currentAlbum.albumArt)
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
					scope.launch {
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
			PlayerControls(
				isPlaying = playbackState.isPlaying,
				onPlayPause = { viewModel.playMedia(currentSong.toSong) },
				skipNextClick = { viewModel.playNext() },
				skipPrevClick = { viewModel.playPrevious() },
				playIcon = {
					val playIcon by viewModel.playIcon.collectAsState()
					Icon(
						imageVector = playIcon,
						contentDescription = null
					)
				}
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
	)
}

@ExperimentalMaterialApi
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
				songsList = songsList.data?.filter { it.mediaId == currentAlbum.mediaId }
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
					backgroundColor = MaterialTheme.colors.primaryVariant.compositeOverBackground(),
					contentPadding = PaddingValues(vertical = 16.dp),
					onClick = openPlayer
				) {
					Icon(
						imageVector = Icons.Rounded.KeyboardArrowDown,
						contentDescription = null
					)
				}
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