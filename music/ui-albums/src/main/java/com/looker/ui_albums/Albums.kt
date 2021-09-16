package com.looker.ui_albums

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowDropUp
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.looker.components.BottomSheets
import com.looker.components.HowlSurface
import com.looker.components.rememberDominantColorState
import com.looker.data_music.data.AlbumsRepository
import com.looker.data_music.data.SongsRepository
import com.looker.domain_music.Album
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
            AlbumsRepository(),
            SongsRepository()
        )
    )
) {
    HowlSurface(modifier = modifier) {

        val context = LocalContext.current

        val state = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
        val scope = rememberCoroutineScope()

        val currentAlbum by viewModel.currentAlbum.observeAsState(Album(0))
        val albumsList by viewModel.albumsList.observeAsState(listOf())
        val songsList by viewModel.songsList.observeAsState(listOf())
        val handleIcon by viewModel.handleIcon.observeAsState(Icons.Rounded.ArrowDropUp)

        LaunchedEffect(context) {
            launch { viewModel.getAllSongs(context) }
        }

        BottomSheets(
            state = state,
            sheetContent = {
                val dominantColor = rememberDominantColorState()

                LaunchedEffect(currentAlbum) {
                    launch {
                        dominantColor.updateColorsFromImageUrl(currentAlbum.albumArt)
                        viewModel.getSongsPerAlbum()
                    }
                }
                viewModel.getIcon(state)

                AlbumsBottomSheetContent(
                    currentAlbum = currentAlbum,
                    songsList = songsList,
                    handleIcon = handleIcon,
                    dominantColor = dominantColor.color.copy(0.4f)
                )
            },
            content = {

                LaunchedEffect(albumsList) {
                    launch { viewModel.getAlbumsList(context) }
                }

                AlbumsList(
                    albumsList = albumsList,
                    onAlbumClick = { album ->
                        scope.launch { viewModel.onAlbumClick(state, album) }
                    }
                )
            }
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AlbumsList(
    albumsList: List<Album>,
    onAlbumClick: (Album) -> Unit
) {
    LazyVerticalGrid(cells = GridCells.Adaptive(200.dp)) {
        items(albumsList) { album ->
            AlbumsCard(album = album) {
                onAlbumClick(album)
            }
        }
    }
}