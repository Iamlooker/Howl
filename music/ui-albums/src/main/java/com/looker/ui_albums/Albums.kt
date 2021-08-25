package com.looker.ui_albums

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.looker.data_albums.data.Album
import com.looker.ui_albums.components.AlbumsCard

@Composable
fun Albums(viewModel: AlbumsViewModel = viewModel()) {
    Albums(albumsList = viewModel.getAlbumsList())
}

@Composable
private fun Albums(
    albumsList: List<Album>
) {
    AlbumsList(albumsList = albumsList)
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AlbumsList(
    albumsList: List<Album>
) {
    LazyVerticalGrid(cells = GridCells.Fixed(2)) {
        items(albumsList) { album ->
            AlbumsCard(album = album)
        }
    }
}