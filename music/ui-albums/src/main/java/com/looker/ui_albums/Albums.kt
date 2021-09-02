package com.looker.ui_albums

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.rememberInsetsPaddingValues
import com.looker.components.backgroundGradient
import com.looker.components.rememberDominantColorState
import com.looker.data_music.data.Album
import com.looker.data_music.data.Song
import com.looker.data_music.data.SongsRepository
import com.looker.ui_albums.components.AlbumsCard
import com.looker.ui_albums.components.AlbumsItem
import com.looker.ui_albums.components.artworkUri
import com.looker.ui_songs.SongsList
import kotlinx.coroutines.launch

@Composable
fun Albums() {
    Albums(modifier = Modifier.fillMaxSize())
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun Albums(
    modifier: Modifier = Modifier,
    viewModel: AlbumsViewModel = viewModel(
        factory = AlbumsViewModelFactory(
            com.looker.data_music.data.AlbumsRepository(),
            SongsRepository()
        )
    )
) {
    Surface(
        modifier = modifier,
        color = MaterialTheme.colors.background
    ) {

        val context = LocalContext.current

        val state = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
        val scope = rememberCoroutineScope()

        val currentAlbum = viewModel.currentAlbum.value
        val albumsList = remember {
            mutableStateOf<List<Album>>(listOf())
        }
        val songsList = remember {
            mutableStateOf<List<Song>>(listOf())
        }

        LaunchedEffect(albumsList) {
            albumsList.value = viewModel.getAlbumsList(context)
        }

        AlbumsList(
            albumsList = albumsList.value,
            onAlbumClick = {
                viewModel.currentAlbum.value = this
                scope.launch {
                    state.animateTo(
                        ModalBottomSheetValue.HalfExpanded,
                        spring(
                            dampingRatio = Spring.DampingRatioLowBouncy,
                            stiffness = Spring.StiffnessLow
                        )
                    )
                }
            }
        )

        val sheetColor = rememberDominantColorState()

        BottomSheets(
            state = state,
            sheetContent = {
                Column(
                    modifier = Modifier.backgroundGradient(sheetColor.color.copy(0.4f))
                ) {

                    val bottomSheetContext = LocalContext.current

                    LaunchedEffect(currentAlbum) {
                        launch {
                            songsList.value = viewModel.getSongsPerAlbum(bottomSheetContext)
                            sheetColor.updateColorsFromImageUrl(currentAlbum.albumId.artworkUri.toString())
                        }
                    }
                    ShowHint(viewModel.getIcon(state), state.overflow.value)
                    AlbumsItem(
                        modifier = Modifier.fillMaxWidth(),
                        album = currentAlbum,
                        imageHeight = 250.dp,
                        imageWidth = 250.dp,
                        imageShape = MaterialTheme.shapes.large
                    )
                    SongsList(songsList = songsList.value)
                    Spacer(Modifier.height(50.dp))
                }
            }
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AlbumsList(
    albumsList: List<Album>,
    onAlbumClick: Album.() -> Unit
) {
    LazyVerticalGrid(
        cells = GridCells.Adaptive(200.dp),
        contentPadding = rememberInsetsPaddingValues(
            insets = LocalWindowInsets.current.systemBars,
            applyTop = true
        )
    ) {
        items(albumsList) { album ->
            AlbumsCard(album = album) {
                album.onAlbumClick()
            }
        }
    }
}

@ExperimentalMaterialApi
@Composable
fun BottomSheets(
    modifier: Modifier = Modifier,
    state: ModalBottomSheetState,
    sheetContent: @Composable ColumnScope.() -> Unit,
    content: @Composable () -> Unit = {}
) {
    ModalBottomSheetLayout(
        modifier = modifier,
        sheetElevation = 0.dp,
        sheetState = state,
        sheetContent = sheetContent,
        content = content,
        sheetBackgroundColor = MaterialTheme.colors.background,
        scrimColor = MaterialTheme.colors.background.copy(0.1f),
        sheetShape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
    )
}

@ExperimentalMaterialApi
@Composable
fun ShowHint(icon: ImageVector, angle: Float) {

    val animatedAngle by animateFloatAsState(targetValue = angle)

    Crossfade(
        targetState = icon,
        animationSpec = tween(500)
    ) { currentIcon ->
        Icon(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .alpha(0.6f)
                .rotate(animatedAngle),
            imageVector = currentIcon,
            contentDescription = "Swipe Action"
        )
    }

}