package com.looker.ui_albums

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.looker.components.HandleIcon
import com.looker.components.backgroundGradient
import com.looker.domain_music.Album
import com.looker.domain_music.Song
import com.looker.ui_albums.components.AlbumsDetailsItem
import com.looker.ui_songs.SongsList

@Composable
fun AlbumsBottomSheetContent(
    modifier: Modifier = Modifier,
    currentAlbum: Album?,
    handleIcon: Float,
    songsList: List<Song>,
    dominantColor: Color = MaterialTheme.colors.surface
) {
    AlbumBottomSheetItem(
        modifier = modifier,
        album = currentAlbum,
        handleIcon = handleIcon,
        albumDominantColor = dominantColor,
        songsList = songsList
    )
}

@Composable
fun AlbumBottomSheetItem(
    modifier: Modifier = Modifier,
    album: Album?,
    handleIcon: Float,
    albumDominantColor: Color,
    songsList: List<Song>
) {
    Column(modifier = modifier.backgroundGradient(albumDominantColor)) {
        HandleIcon(angle = handleIcon, backgroundColor = Color.Transparent)
        AlbumHeader(album = album)
        AlbumSongsList(songsList = songsList)
    }
}

@Composable
fun AlbumHeader(album: Album?) {
    AlbumsDetailsItem(
        albumArt = album?.albumArt,
        albumName = album?.albumName,
        artistName = album?.artistName
    )
}

@Composable
fun AlbumSongsList(
    songsList: List<Song>
) {
    SongsList(songsList = songsList)
    Spacer(modifier = Modifier.height(50.dp))
}