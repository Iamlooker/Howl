package com.looker.howlmusic

import android.app.Application
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.RepeatOne
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import coil.ImageLoader
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.navigationBarsPadding
import com.google.accompanist.insets.statusBarsHeight
import com.google.accompanist.insets.statusBarsPadding
import com.looker.components.HandleIcon
import com.looker.components.SheetsState
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
class HowlApp : Application()

@Composable
fun App(imageLoader: ImageLoader) {
    val context = LocalContext.current
    var canReadStorage by remember { mutableStateOf(checkReadPermission(context)) }

    HowlMusicTheme {
        if (canReadStorage) ProvideWindowInsets { AppContent(imageLoader) }
        else OnBoardingPage { canReadStorage = it }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AppContent(imageLoader: ImageLoader, viewModel: HowlViewModel = viewModel()) {

    val scope = rememberCoroutineScope()
    val backdropState = rememberBackdropScaffoldState(BackdropValue.Concealed)
    val backdropValue by viewModel.backdropValue.observeAsState(SheetsState.HIDDEN)

    val playing by viewModel.playing.observeAsState(false)
    val enableGesture by viewModel.enableGesture.observeAsState(true)

    val currentSong by viewModel.currentSong.observeAsState()

    LaunchedEffect(
        backdropState.currentValue.name
    ) { launch { viewModel.setBackdropValue(backdropState) } }

    Backdrop(
        modifier = Modifier,
        state = backdropState,
        backdropValue = backdropValue,
        playing = playing,
        enableGesture = enableGesture,
        albumArt = currentSong?.albumArt,
        header = {

            val toggleIcon by viewModel.toggleIcon.observeAsState(Icons.Rounded.PlayArrow)

            LaunchedEffect(backdropValue, playing) {
                launch {
                    viewModel.setToggleIcon(
                        backdropValue
                    )
                }
            }

            PlayerHeader(
                icon = toggleIcon,
                albumArt = currentSong?.albumArt,
                imageLoader = imageLoader,
                songName = currentSong?.songName,
                artistName = currentSong?.artistName,
                toggled = playing,
                toggleAction = { viewModel.onToggle(backdropValue) }
            )
        },
        frontLayerContent = {

            val handleIcon by viewModel.handleIcon.observeAsState(2f)

            LaunchedEffect(backdropValue) { launch { viewModel.setHandleIcon(backdropValue) } }

            FrontLayer(
                imageLoader = imageLoader,
                handleIcon = handleIcon,
                onSongClick = { song -> viewModel.onSongClicked(song) },
                openPlayer = { scope.launch { backdropState.reveal() } },
                onAlbumSheetState = {
                    if (backdropValue == SheetsState.HIDDEN) viewModel.gestureState(it)
                    else viewModel.gestureState(true)
                }
            )
        },
        backLayerContent = {

            val progress by viewModel.progress.observeAsState(0f)

            Controls(
                isPlaying = playing,
                progress = progress,
                onPlayPause = { viewModel.onPlayPause() },
                skipNextClick = { viewModel.playNext() },
                skipPrevClick = { viewModel.playPrevious() },
                onSeek = { seekTo -> viewModel.onSeek(seekTo) },
                openQueue = { scope.launch { backdropState.conceal() } }
            )
        }
    )
}

@Composable
fun FrontLayer(
    modifier: Modifier = Modifier,
    imageLoader: ImageLoader,
    handleIcon: Float,
    openPlayer: () -> Unit,
    onSongClick: (Song) -> Unit,
    onAlbumSheetState: (Boolean) -> Unit
) {

    val navController = rememberNavController()
    val items = listOf(
        HomeScreens.SONGS,
        HomeScreens.ALBUMS
    )

    Scaffold(
        bottomBar = {
            BottomAppBar(
                modifier = Modifier.navigationBarsPadding(),
                navController = navController,
                items = items
            )
        }
    ) { bottomNavigationPadding ->
        Column(
            modifier
                .background(MaterialTheme.colors.surface)
                .padding(bottomNavigationPadding)
        ) {
            HandleIcon(handleIcon) { openPlayer() }
            HomeNavGraph(
                navController = navController,
                imageLoader = imageLoader,
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
    imageLoader: ImageLoader,
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
        songName = songName ?: "Unknown",
        artistName = artistName ?: "Unknown",
        albumArt = albumArt,
        imageLoader = imageLoader,
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