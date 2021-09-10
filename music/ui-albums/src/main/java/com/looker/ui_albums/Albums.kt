package com.looker.ui_albums

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.rememberInsetsPaddingValues
import com.looker.components.BottomSheets
import com.looker.components.ComponentConstants.artworkUri
import com.looker.components.HowlSurface
import com.looker.components.rememberDominantColorState
import com.looker.data_music.data.Album
import com.looker.data_music.data.Song
import com.looker.data_music.data.SongsRepository
import com.looker.ui_albums.components.AlbumsCard
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
    HowlSurface(modifier = modifier) {

        val context = LocalContext.current

        val state = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
        val scope = rememberCoroutineScope()

        val currentAlbum = viewModel.currentAlbum
        val albumsList = remember {
            mutableStateOf<List<Album>>(listOf())
        }
        var songsList by remember {
            mutableStateOf<List<Song>>(listOf())
        }

        LaunchedEffect(albumsList) {
            albumsList.value = viewModel.getAlbumsList(context)
        }

        AlbumsList(
            albumsList = albumsList.value,
            onAlbumClick = {
                viewModel.currentAlbum = this
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

        BottomSheets(
            state = state,
            sheetContent = {
                val bottomSheetContext = LocalContext.current
                val dominantColor = rememberDominantColorState()

                LaunchedEffect(currentAlbum) {
                    launch {
                        dominantColor.updateColorsFromImageUrl(currentAlbum.albumId.artworkUri.toString())
                        songsList = viewModel.getSongsPerAlbum(bottomSheetContext)
                    }
                }
                AlbumsBottomSheet(
                    currentAlbum = viewModel.currentAlbum,
                    songsList = songsList,
                    iconState = viewModel.getIcon(state),
                    dominantColor = dominantColor.color.copy(0.4f)
                )
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