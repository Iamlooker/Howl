package com.looker.ui_albums

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.rememberInsetsPaddingValues
import com.looker.data_albums.data.Album
import com.looker.ui_albums.components.AlbumsCard
import com.looker.ui_albums.components.AlbumsItem
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Albums(viewModel: AlbumsViewModel = viewModel()) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background
    ) {

        val state = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
        val scope = rememberCoroutineScope()

        var album by remember {
            mutableStateOf(Album(0))
        }

        BottomSheets(
            state = state,
            onSheetContent = {
                Spacer(Modifier.height(50.dp))
                AlbumsItem(
                    modifier = Modifier.fillMaxWidth(),
                    album = album,
                    imageSize = 250.dp
                )
            }
        ) {
            AlbumsList(albumsList = viewModel.getAlbumsList(), onAlbumClick = {
                album = it
                scope.launch { state.show() }
            })
        }
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AlbumsList(
    albumsList: List<Album>,
    onAlbumClick: (Album) -> Unit = {}
) {
    LazyVerticalGrid(
        cells = GridCells.Fixed(2),
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
    state: ModalBottomSheetState,
    onSheetContent: @Composable ColumnScope.() -> Unit,
    content: @Composable () -> Unit
) {

    ModalBottomSheetLayout(
        sheetState = state,
        sheetContent = onSheetContent,
        content = content,
        sheetBackgroundColor = MaterialTheme.colors.background
    )
}