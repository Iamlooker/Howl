package com.looker.ui_albums

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.ImageLoader
import com.looker.components.BottomSheets
import com.looker.components.rememberDominantColorState
import com.looker.data_music.data.AlbumsRepository
import com.looker.data_music.data.SongsRepository
import com.looker.domain_music.Album
import com.looker.ui_albums.components.AlbumsCard
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Albums(
    imageLoader: ImageLoader,
    viewModel: AlbumsViewModel = viewModel(
        factory = AlbumsViewModelFactory(
            AlbumsRepository(),
            SongsRepository()
        )
    ),
    onStateChange: (Boolean) -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val state = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)

    LaunchedEffect(context) { launch { viewModel.getAllSongs(context) } }
    BottomSheets(
        state = state,
        sheetContent = {

            val dominantColor = rememberDominantColorState()
            val currentAlbum by viewModel.currentAlbum.collectAsState()
            val songsList by viewModel.songsList.collectAsState()
            LaunchedEffect(currentAlbum) {
                launch {
                    dominantColor.updateColorsFromImageUrl(currentAlbum.albumArt)
                    viewModel.updateSongsList(currentAlbum.albumId)
                }
            }

            val handleIcon by viewModel.handleIcon.collectAsState()
            LaunchedEffect(state.currentValue) {
                launch {
                    onStateChange(!state.isVisible)
                    viewModel.getIcon(state)
                }
            }

            AlbumsBottomSheetContent(
                currentAlbum = currentAlbum,
                imageLoader = imageLoader,
                songsList = songsList,
                handleIcon = handleIcon,
                dominantColor = dominantColor.color.copy(0.4f)
            )
        },
        content = {
            val albumsList by viewModel.albumsList.collectAsState()
            LaunchedEffect(albumsList) { launch { viewModel.getAlbumsList(context) } }

            AlbumsList(
                albumsList = albumsList,
                imageLoader = imageLoader,
                onAlbumClick = {
                    scope.launch {
                        viewModel.onAlbumClick(it)
                        state.show()
                    }
                }
            )
        }
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AlbumsList(
    albumsList: List<Album>,
    imageLoader: ImageLoader,
    onAlbumClick: (Album) -> Unit
) {

    val width = with(LocalConfiguration.current) { screenWidthDp.dp / 2 - 16.dp }

    LazyVerticalGrid(cells = GridCells.Adaptive(200.dp)) {
        items(albumsList) {
            AlbumsCard(
                imageLoader = imageLoader,
                album = it,
                cardWidth = width
            ) { onAlbumClick(it) }
        }
    }
}