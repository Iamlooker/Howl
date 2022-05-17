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
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import coil.compose.AsyncImage
import com.looker.components.*
import com.looker.components.localComposers.LocalDurations
import com.looker.constants.Resource
import com.looker.core_model.Album
import com.looker.core_model.Song
import com.looker.feature_player.PlayerHeader
import com.looker.howlmusic.ui.components.*
import com.looker.howlmusic.utils.extension.isPlaying
import com.looker.howlmusic.utils.extension.toSong
import com.looker.ui_albums.AlbumsBottomSheetContent
import com.looker.ui_player.PlayerControls
import com.looker.ui_player.components.SeekBar
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Home(
	navController: NavHostController,
	items: Array<HomeScreens>,
	viewModel: HowlViewModel = viewModel()
) {

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
				navController = navController,
				items = items,
				bottomSheetState = bottomSheetState,
				songsList = songsList,
				albumsList = albumsList,
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
				skipNextClick = { viewModel.playNext() },
				skipPrevClick = { viewModel.playPrevious() },
				playButton = {
					val buttonShape by animateIntAsState(
						targetValue = if (playbackState.isPlaying) 50 else 15,
						animationSpec = tweenAnimation(LocalDurations.current.crossFade)
					)
					ShapedIconButton(
						modifier = Modifier
							.height(60.dp)
							.weight(3f)
							.graphicsLayer {
								shape = RoundedCornerShape(buttonShape)
								clip = true
							},
						onClick = { viewModel.playMedia(currentSong.toSong) },
						backgroundColor = MaterialTheme.colors.primaryVariant.overBackground(0.9f),
						contentColor = MaterialTheme.colors.onPrimary
					) {
						val playIcon by viewModel.playIcon.collectAsState()
						Icon(
							imageVector = playIcon,
							contentDescription = null
						)
					}
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
	navController: NavHostController,
	items: Array<HomeScreens>,
	bottomSheetState: ModalBottomSheetState,
	songsList: Resource<List<Song>>,
	albumsList: List<Album>,
	currentAlbum: Album,
	albumsDominantColor: Color,
	openPlayer: () -> Unit,
	onSongClick: (Song) -> Unit,
	onAlbumClick: (Album) -> Unit,
) {
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
				val navBackStackEntry by navController.currentBackStackEntryAsState()
				val currentDestination = navBackStackEntry?.destination
				BottomAppBar(
					modifier = Modifier.windowInsetsBottomHeight(
						WindowInsets.navigationBars.add(WindowInsets(bottom = 56.dp))
					)
				) {
					items.forEach { screen ->
						BottomNavigationItems(
							modifier = Modifier.navigationBarsPadding(),
							icon = screen.icon,
							label = screen.title,
							selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
							onSelected = {
								navController.navigate(screen.route) {
									popUpTo(navController.graph.findStartDestination().id) {
										saveState = true
									}
									launchSingleTop = true
									restoreState = true
								}
							}
						)
					}
				}
			},
			floatingActionButton = {
				ShapedIconButton(
					backgroundColor = MaterialTheme.colors.primaryVariant.overBackground(),
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
				HandleIcon { openPlayer() }
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