package com.looker.ui_albums

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.rememberInsetsPaddingValues
import com.looker.components.backgroundGradient
import com.looker.components.rememberDominantColorState
import com.looker.data_albums.data.Album
import com.looker.data_albums.data.AlbumsRepository
import com.looker.data_songs.data.SongsRepository
import com.looker.ui_albums.components.AlbumsCard
import com.looker.ui_albums.components.AlbumsExtensions.artworkUri
import com.looker.ui_albums.components.AlbumsItem
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
        factory = AlbumsViewModelFactory(AlbumsRepository(), SongsRepository())
    )
) {
    Surface(
        modifier = modifier,
        color = MaterialTheme.colors.background
    ) {

        val state = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
        val scope = rememberCoroutineScope()

        val albumsList = viewModel.getAlbumsList(LocalContext.current)
        val currentAlbum = viewModel.currentAlbum.value

        AlbumsList(
            albumsList = albumsList,
            onAlbumClick = {
                viewModel.currentAlbum.value = it
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

        LaunchedEffect(currentAlbum) {
            launch {
                sheetColor.updateColorsFromImageUrl(currentAlbum.albumId.artworkUri.toString())
            }
        }

        BottomSheets(
            state = state,
            sheetContent = {
                Column(
                    modifier = Modifier.backgroundGradient(sheetColor.color.copy(0.4f))
                ) {
                    Spacer(Modifier.height(50.dp))
                    AlbumsItem(
                        modifier = Modifier.fillMaxWidth(),
                        album = currentAlbum,
                        imageSize = 250.dp
                    )
                    SongsList(
                        songsList = viewModel.getSongsPerAlbum(
                            LocalContext.current,
                            currentAlbum.albumId
                        )
                    )
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
    onAlbumClick: (Album) -> Unit
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
                onAlbumClick(album)
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
        sheetShape = RoundedCornerShape(topStart = 14.dp, topEnd = 14.dp)
    )
}