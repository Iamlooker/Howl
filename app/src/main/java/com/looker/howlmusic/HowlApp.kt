package com.looker.howlmusic

import android.app.Application
import android.app.WallpaperManager
import android.content.Intent
import android.graphics.Bitmap
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowDropDown
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
import androidx.core.net.toUri
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.navigationBarsPadding
import com.google.accompanist.insets.statusBarsHeight
import com.google.android.exoplayer2.SimpleExoPlayer
import com.looker.components.ComponentConstants.artworkUri
import com.looker.components.HowlSurface
import com.looker.components.rememberDominantColorState
import com.looker.data_music.data.Song
import com.looker.howlmusic.ui.components.*
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
fun App(player: SimpleExoPlayer) {
    val context = LocalContext.current
    var canReadStorage by remember { mutableStateOf(checkReadPermission(context)) }
    val wallpaperManager = WallpaperManager.getInstance(context)

    HowlMusicTheme {
        if (canReadStorage) {
            val wallpaperBitmap = wallpaperManager.drawable.toBitmap()
            AppTheme(wallpaperBitmap, player = player)
        } else OnBoardingPage { canReadStorage = it }
    }
}

@Composable
fun AppTheme(wallpaper: Bitmap? = null, player: SimpleExoPlayer) {
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
                AppContent(player = player)
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AppContent(viewModel: HowlViewModel = viewModel(), player: SimpleExoPlayer) {

    val context = LocalContext.current

    val playerService = PlayerService()

    val currentSong by viewModel.currentSong.observeAsState(Song("".toUri(), 0))

    val intent = Intent(context, playerService::class.java)

    LaunchedEffect(intent) {
        context.startForegroundService(intent)
        playerService.currentSong = currentSong
    }

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
        val handleIcon by viewModel.handleIcon.observeAsState(Icons.Rounded.ArrowDropDown)
        val shuffleIcon by remember { mutableStateOf(Icons.Rounded.Shuffle) }

        val currentFraction = backdropState.currentFraction
        LaunchedEffect(currentFraction) { viewModel.setHandleIcon(currentFraction) }
        Backdrop(
            modifier = Modifier.padding(it),
            state = backdropState,
            currentFraction = currentFraction,
            playing = playing,
            albumArt = currentSong.albumId.artworkUri,
            header = {
                PlayerHeader(
                    icon = if (currentFraction > 0f) shuffleIcon else playIcon,
                    albumArt = currentSong.albumId.artworkUri,
                    songName = currentSong.songName,
                    artistName = currentSong.artistName,
                    toggled = if (currentFraction > 0f) shuffle else playing,
                    openPlayer = { scope.launch { backdropState.reveal() } },
                    toggleAction = { viewModel.onToggle(currentFraction) }
                )
            },
            frontLayerContent = {
                FrontLayer(
                    navController = navController,
                    handleIcon = handleIcon,
                    onSongClick = { song ->
                        viewModel.onSongClicked(song)
                        playerService.initPlayer(player, song.songUri)
                    },
                    openPlayer = {
                        scope.launch { backdropState.reveal() }
                    }
                )

            },
            backLayerContent = {
                Controls(
                    playIcon = playIcon,
                    progress = progress,
                    onPlayPause = { playerService.togglePlay() },
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
    onSongClick: (Song) -> Unit
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
        HomeNavGraph(
            navController = navController,
            onSongClick = onSongClick
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
    openPlayer: () -> Unit,
    toggleAction: () -> Unit
) {
    Column(modifier) {
        Spacer(Modifier.statusBarsHeight())
        MiniPlayer(
            modifier = Modifier
                .clickable(onClick = openPlayer)
                .padding(vertical = 20.dp),
            songName = songName ?: "Unknown",
            artistName = artistName ?: "Unknown",
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