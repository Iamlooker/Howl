package com.looker.howlmusic

import android.app.Application
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.RepeatOne
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import coil.ImageLoader
import coil.ImageLoaderFactory
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.navigationBarsHeight
import com.google.accompanist.insets.statusBarsHeight
import com.google.accompanist.insets.statusBarsPadding
import com.looker.components.*
import com.looker.components.localComposers.LocalDurations
import com.looker.domain_music.Album
import com.looker.domain_music.Song
import com.looker.howlmusic.ui.components.Backdrop
import com.looker.howlmusic.ui.components.BottomAppBar
import com.looker.howlmusic.ui.components.HomeNavGraph
import com.looker.howlmusic.ui.components.HomeScreens
import com.looker.howlmusic.ui.theme.HowlMusicTheme
import com.looker.howlmusic.utils.checkReadPermission
import com.looker.onboarding.OnBoardingPage
import com.looker.ui_player.MiniPlayer
import com.looker.ui_player.components.PlaybackControls
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.launch

@HiltAndroidApp
class HowlApp : Application(), ImageLoaderFactory {
    override fun newImageLoader(): ImageLoader {
        return ImageLoader.Builder(this)
            .crossfade(400)
            .build()
    }
}

@Composable
fun App() {
    val context = LocalContext.current
    var canReadStorage by remember { mutableStateOf(checkReadPermission(context)) }

    HowlMusicTheme {
        ProvideWindowInsets {
            if (canReadStorage) Home()
            else OnBoardingPage { canReadStorage = it }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Home(viewModel: HowlViewModel = viewModel()) {

    val scope = rememberCoroutineScope()
    val state = rememberBackdropScaffoldState(BackdropValue.Concealed)

    val playing by viewModel.playing.collectAsState()
    val currentSong by viewModel.currentSong.collectAsState()
    val backdropValue by viewModel.backdropValue.collectAsState()
    val enableGesture by viewModel.enableGesture.collectAsState()

    LaunchedEffect(state.currentValue.name) {
        launch { viewModel.setBackdropValue(state.currentValue) }
    }

    Backdrop(
        modifier = Modifier,
        state = state,
        playing = playing,
        enableGesture = enableGesture,
        header = {

            val toggleIcon by viewModel.toggleIcon.collectAsState()
            val backgroundColor = rememberDominantColorState()

            LaunchedEffect(currentSong) {
                launch {
                    backgroundColor.updateColorsFromImageUrl(currentSong.albumArt)
                }
            }

            LaunchedEffect(backdropValue, playing) {
                launch { viewModel.setToggleIcon(backdropValue) }
            }

            val animatedBackgroundScrim by animateColorAsState(
                targetValue = backgroundColor.color.copy(
                    0.3f
                ),
                animationSpec = tweenAnimation(LocalDurations.current.crossFade)
            )

            PlayerHeader(
                modifier = Modifier.backgroundGradient(animatedBackgroundScrim),
                icon = toggleIcon,
                albumArt = currentSong.albumArt,
                songName = currentSong.songName,
                artistName = currentSong.artistName,
                toggled = playing,
                toggleAction = { viewModel.onToggle(backdropValue) }
            )
        },
        frontLayerContent = {

            val handleIcon by viewModel.handleIcon.collectAsState()
            val songsList by viewModel.songsList.collectAsState()
            val albumsList by viewModel.albumsList.collectAsState()

            LaunchedEffect(backdropValue) { launch { viewModel.setHandleIcon(backdropValue) } }

            FrontLayer(
                songsList = songsList,
                albumsList = albumsList,
                handleIcon = handleIcon,
                onSongClick = { songIndex -> viewModel.onSongClicked(songIndex) },
                openPlayer = { scope.launch { state.reveal() } },
                onAlbumSheetState = {
                    if (backdropValue == SheetsState.HIDDEN) viewModel.gestureState(it)
                    else viewModel.gestureState(true)
                }
            )
        },
        backLayerContent = {

            val progress by viewModel.progress.collectAsState()
            Controls(
                isPlaying = playing,
                progress = progress,
                onPlayPause = { viewModel.onPlayPause() },
                skipNextClick = { viewModel.playNext() },
                skipPrevClick = { viewModel.playPrevious() },
                onSeek = { seekTo -> viewModel.onSeek(seekTo) },
                openQueue = { scope.launch { state.conceal() } }
            )
        }
    )
}

@Composable
fun FrontLayer(
    modifier: Modifier = Modifier,
    songsList: List<Song>,
    albumsList: List<Album>,
    handleIcon: Float,
    openPlayer: () -> Unit,
    onSongClick: (Int) -> Unit,
    onAlbumSheetState: (Boolean) -> Unit
) {
    val navController = rememberNavController()

    val items = listOf(
        HomeScreens.SONGS,
        HomeScreens.ALBUMS
    )
    Scaffold(
        bottomBar = {
            Surface {
                BottomAppBar(
                    modifier = Modifier.navigationBarsHeight(56.dp),
                    navController = navController,
                    items = items
                )
            }
        }
    ) { bottomNavigationPadding ->
        Column(modifier.padding(bottomNavigationPadding)) {
            HandleIcon(handleIcon) { openPlayer() }
            HomeNavGraph(
                navController = navController,
                songsList = songsList,
                albumsList = albumsList,
                onSongClick = onSongClick,
                onAlbumsSheetState = onAlbumSheetState
            )
        }
    }
}

@Composable
fun PlayerHeader(
    modifier: Modifier = Modifier,
    albumArt: String?,
    songName: String?,
    artistName: String?,
    icon: ImageVector,
    toggled: Boolean,
    toggleAction: () -> Unit
) {
    MiniPlayer(
        modifier = modifier
            .statusBarsPadding()
            .padding(bottom = 20.dp),
        songName = songName,
        artistName = artistName,
        albumArt = albumArt,
        onImageIcon = icon,
        repeatIcon = Icons.Rounded.RepeatOne,
        toggled = toggled,
        toggleAction = toggleAction
    )
}

@Composable
fun Controls(
    modifier: Modifier = Modifier,
    progress: Float,
    isPlaying: Boolean,
    onPlayPause: () -> Unit,
    skipNextClick: () -> Unit,
    onSeek: (Float) -> Unit,
    openQueue: () -> Unit,
    skipPrevClick: () -> Unit
) {
    Column(modifier) {
        PlaybackControls(
            isPlaying = isPlaying,
            progressValue = progress,
            onPlayPause = { onPlayPause() },
            skipNextClick = skipNextClick,
            skipPrevClick = skipPrevClick,
            onSeek = { seekTo -> onSeek(seekTo) },
            openQueue = openQueue
        )
        Spacer(Modifier.statusBarsHeight())
    }
}