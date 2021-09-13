package com.looker.howlmusic

import android.app.WallpaperManager
import android.graphics.Bitmap
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Shuffle
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
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
import com.looker.components.HowlSurface
import com.looker.components.rememberDominantColorState
import com.looker.howlmusic.ui.components.*
import com.looker.howlmusic.ui.theme.HowlMusicTheme
import com.looker.howlmusic.ui.theme.WallpaperTheme
import com.looker.onboarding.OnBoardingPage
import com.looker.ui_player.MiniPlayer
import com.looker.ui_player.components.PlaybackControls
import kotlinx.coroutines.launch

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

@Composable
fun AppTheme(wallpaper: Bitmap? = null) {
    val dominantColor = rememberDominantColorState()

    LaunchedEffect(wallpaper) {
        dominantColor.updateColorsFromBitmap(wallpaper)
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
    val items = listOf(
        HomeScreens.SONGS,
        HomeScreens.ALBUMS
    )
    val scope = rememberCoroutineScope()
    val navController = rememberNavController()
    val backdropState = rememberBackdropScaffoldState(BackdropValue.Concealed)
    Scaffold(
        bottomBar = {
            BottomAppBar(
                navController = navController,
                items = items
            )
        }
    ) {
        val playing by viewModel.playing.observeAsState(false)
        val playIcon by viewModel.playIcon.observeAsState(Icons.Rounded.PlayArrow)
        val progress by viewModel.progress.observeAsState(0f)
        val shuffle by viewModel.shuffle.observeAsState(false)
        val shuffleIcon by remember { mutableStateOf(Icons.Rounded.Shuffle) }
        val albumArt = "https://picsum.photos/4000/3000"

        val currentFraction = backdropState.currentFraction
        Backdrop(
            modifier = Modifier.padding(it),
            state = backdropState,
            currentFraction = currentFraction,
            playing = playing,
            albumArt = albumArt,
            header = {
                PlayerHeader(
                    icon = if (currentFraction > 0f) shuffleIcon else playIcon,
                    albumArt = albumArt,
                    toggled = if (currentFraction > 0f) shuffle else playing,
                    openPlayer = { scope.launch { backdropState.reveal() } },
                    toggleAction = { viewModel.onToggle(currentFraction) }
                )
            },
            frontLayerContent = {
                FrontLayer(
                    navController = navController,
                    handleIcon = viewModel.handleIcon(currentFraction)
                ) {
                    scope.launch { backdropState.reveal() }
                }

            },
            backLayerContent = {
                Controls(
                    playIcon = playIcon,
                    progress = progress,
                    onPlayPause = { viewModel.onPlayPause() },
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
    openPlayer: () -> Unit
) {
    Column(modifier) {
        Crossfade(handleIcon) { icon ->
            Icon(
                modifier = Modifier
                    .height(40.dp)
                    .fillMaxWidth()
                    .clickable(onClick = openPlayer)
                    .align(Alignment.CenterHorizontally)
                    .background(MaterialTheme.colors.background),
                imageVector = icon,
                contentDescription = "Pull Down"
            )
        }
        HomeNavGraph(navController = navController)
    }
}

@Composable
fun PlayerHeader(
    modifier: Modifier = Modifier,
    albumArt: Any,
    songName: String = "No Name",
    artistName: String = "No Name",
    icon: ImageVector,
    toggled: Boolean,
    openPlayer: () -> Unit,
    toggleAction: () -> Unit
) {
    Column(modifier) {
        Spacer(Modifier.statusBarsHeight())
        MiniPlayer(
            modifier = Modifier
                .clickable(onClick = openPlayer)
                .padding(20.dp),
            songName = songName,
            artistName = artistName,
            albumArt = albumArt,
            icon = icon,
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
    onSeek: (Float) -> Unit,
    openQueue: () -> Unit
) {
    Column(modifier) {
        PlaybackControls(
            playIcon = playIcon,
            progressValue = progress,
            onPlayPause = { onPlayPause() },
            onSeek = { seekTo -> onSeek(seekTo) },
            openQueue = openQueue
        )
        Spacer(Modifier.statusBarsHeight())
    }
}