package com.looker.howlmusic

import android.app.Application
import android.app.WallpaperManager
import android.content.Intent
import android.graphics.Bitmap
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.RepeatOne
import androidx.compose.material.icons.rounded.Shuffle
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.navigationBarsPadding
import com.google.accompanist.insets.statusBarsHeight
import com.looker.components.HandleIcon
import com.looker.components.HowlSurface
import com.looker.components.rememberDominantColorState
import com.looker.domain_music.Song
import com.looker.domain_music.emptySong
import com.looker.howlmusic.ui.components.Backdrop
import com.looker.howlmusic.ui.components.BottomAppBar
import com.looker.howlmusic.ui.components.HomeNavGraph
import com.looker.howlmusic.ui.components.HomeScreens
import com.looker.howlmusic.ui.theme.HowlMusicTheme
import com.looker.howlmusic.ui.theme.WallpaperTheme
import com.looker.howlmusic.utils.checkReadPermission
import com.looker.onboarding.OnBoardingPage
import com.looker.player_service.service.PlayerService
import com.looker.ui_player.MiniPlayer
import com.looker.ui_player.components.PlaybackControls
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.launch

@HiltAndroidApp
class HowlApp : Application()

@Composable
fun App() {
    val context = LocalContext.current
    var canReadStorage by remember { mutableStateOf(checkReadPermission(context)) }
    val wallpaperManager = WallpaperManager.getInstance(context)

    HowlMusicTheme {
        if (canReadStorage) {
            val wallpaperBitmap = wallpaperManager.drawable.toBitmap()
            AppTheme(wallpaperBitmap)
        } else OnBoardingPage { canReadStorage = it }
    }
}

@OptIn(ExperimentalComposeApi::class)
@Composable
fun AppTheme(wallpaper: Bitmap? = null) {
    val dominantColor = rememberDominantColorState()

    LaunchedEffect(wallpaper) {
        launch { dominantColor.updateColorsFromBitmap(wallpaper) }
    }

    WallpaperTheme(dominantColor) {
        ProvideWindowInsets {
            HowlSurface(
                modifier = Modifier
                    .fillMaxSize()
                    .navigationBarsPadding()
            ) {
                AppContent()
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AppContent(viewModel: HowlViewModel = viewModel()) {

    val context = LocalContext.current

    SideEffect {
        val playerService = PlayerService()
        val intent = Intent(context, playerService::class.java)
        context.startForegroundService(intent)
    }

    LaunchedEffect(context) { launch { viewModel.buildExoPlayer(context) } }

    val items = listOf(
        HomeScreens.SONGS,
        HomeScreens.ALBUMS
    )

    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            BottomAppBar(
                navController = navController,
                items = items
            )
        }
    ) { bottomNavigationPadding ->
        val scope = rememberCoroutineScope()

        val backdropState = rememberBackdropScaffoldState(BackdropValue.Concealed)

        val currentSong by viewModel.currentSong.observeAsState(emptySong)

        val playing by viewModel.playing.observeAsState(false)
        val playIcon by viewModel.playIcon.observeAsState(Icons.Rounded.PlayArrow)
        val progress by viewModel.progress.observeAsState(0f)
        val shuffle by viewModel.shuffle.observeAsState(false)
        val handleIcon by viewModel.handleIcon.observeAsState(Icons.Rounded.KeyboardArrowDown)
        val shuffleIcon by remember { mutableStateOf(Icons.Rounded.Shuffle) }
        val enableGesture by viewModel.enableGesture.observeAsState(false)
        val seconds by viewModel.clock.observeAsState(0)

        val playerVisible = viewModel.playerVisible(backdropState)

        val backdropValue = viewModel.currentState(backdropState)

        LaunchedEffect(backdropValue) { launch { viewModel.setHandleIcon(backdropValue) } }
        LaunchedEffect(currentSong) { launch { viewModel.gestureState(true) } }

        Backdrop(
            modifier = Modifier.padding(bottomNavigationPadding),
            state = backdropState,
            backdropValue = viewModel.currentState(backdropState),
            playing = playing,
            enableGesture = enableGesture,
            albumArt = currentSong.albumArt,
            header = {

                LaunchedEffect(seconds) {
                    launch {
                        viewModel.updateProgress()
                        viewModel.updateTime()
                    }
                }

                PlayerHeader(
                    icon = if (playerVisible) shuffleIcon else playIcon,
                    albumArt = currentSong.albumArt,
                    songName = currentSong.songName,
                    artistName = currentSong.artistName,
                    toggled = if (playerVisible) shuffle else playing,
                    toggleAction = { viewModel.onToggle(playerVisible) }
                )
            },
            frontLayerContent = {
                FrontLayer(
                    navController = navController,
                    handleIcon = handleIcon,
                    onSongClick = { song -> viewModel.onSongClicked(song) },
                    openPlayer = { scope.launch { backdropState.reveal() } },
                    onAlbumSheetState = { viewModel.gestureState(it) }
                )
            },
            backLayerContent = {
                Controls(
                    playIcon = playIcon,
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
}

@Composable
fun FrontLayer(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    handleIcon: ImageVector,
    openPlayer: () -> Unit,
    onSongClick: (Song) -> Unit,
    onAlbumSheetState: (Boolean) -> Unit
) {
    Column(modifier.background(MaterialTheme.colors.background)) {
        HandleIcon(handleIcon) { openPlayer() }
        HomeNavGraph(
            navController = navController,
            onSongClick = onSongClick,
            onAlbumsSheetState = onAlbumSheetState
        )
    }
}

@Composable
fun PlayerHeader(
    modifier: Modifier = Modifier,
    albumArt: Any,
    songName: String?,
    artistName: String?,
    icon: ImageVector,
    toggled: Boolean,
    toggleAction: () -> Unit
) {
    Column(modifier) {
        Spacer(Modifier.statusBarsHeight())
        MiniPlayer(
            modifier = Modifier.padding(bottom = 20.dp),
            songName = songName ?: "Unknown",
            artistName = artistName ?: "Unknown",
            albumArt = albumArt,
            onImageIcon = icon,
            repeatIcon = Icons.Rounded.RepeatOne,
            toggled = toggled,
            toggleAction = toggleAction
        )
    }
}

@Composable
fun Controls(
    modifier: Modifier = Modifier,
    playIcon: ImageVector,
    progress: Float,
    onPlayPause: () -> Unit,
    skipNextClick: () -> Unit,
    onSeek: (Float) -> Unit,
    openQueue: () -> Unit,
    skipPrevClick: () -> Unit
) {
    Column(modifier) {
        PlaybackControls(
            playIcon = playIcon,
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