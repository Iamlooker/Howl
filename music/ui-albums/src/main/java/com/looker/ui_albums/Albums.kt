package com.looker.ui_albums

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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.looker.components.BottomSheets
import com.looker.components.rememberDominantColorState
import com.looker.domain_music.Album
import com.looker.ui_albums.components.AlbumsCard
import kotlinx.coroutines.launch

@Composable
fun Albums(albumsList: List<Album>, onStateChange: (Boolean) -> Unit) {
    Albums(
        modifier = Modifier.fillMaxSize(),
        albumsList = albumsList,
        onStateChange = onStateChange
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun Albums(
    modifier: Modifier = Modifier,
    albumsList: List<Album>,
    viewModel: AlbumsViewModel = viewModel(),
    onStateChange: (Boolean) -> Unit
) {

    val scope = rememberCoroutineScope()
    val state = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)

    BottomSheets(
        modifier = modifier,
        state = state,
        sheetContent = {

            val dominantColor = rememberDominantColorState()
            val currentAlbum by viewModel.currentAlbum.collectAsState()
            val songsList by viewModel.songsList.collectAsState()
            LaunchedEffect(currentAlbum) {
                launch { dominantColor.updateColorsFromImageUrl(currentAlbum.albumArt) }
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
                songsList = songsList,
                handleIcon = handleIcon,
                dominantColor = dominantColor.color.copy(0.4f)
            )
        },
        content = {
            AlbumsList(
                albumsList = albumsList,
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
    onAlbumClick: (Album) -> Unit
) {

    val width = with(LocalConfiguration.current) { screenWidthDp.dp / 2 - 16.dp }

    LazyVerticalGrid(cells = GridCells.Adaptive(200.dp)) {
        items(albumsList) {
            AlbumsCard(album = it, cardWidth = width) {
                onAlbumClick(it)
            }
        }
    }
}