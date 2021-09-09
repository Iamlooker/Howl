package com.looker.ui_albums

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.looker.components.backgroundGradient
import com.looker.data_music.data.Album
import com.looker.data_music.data.Song
import com.looker.ui_albums.components.AlbumsItem
import com.looker.ui_songs.SongsList

@ExperimentalMaterialApi
@Composable
fun AlbumsBottomSheet(
    modifier: Modifier = Modifier,
    currentAlbum: Album,
    iconState: ImageVector,
    songsList: List<Song>,
    dominantColor: Color = MaterialTheme.colors.surface
) {
    AlbumBottomSheetItem(
        modifier = modifier,
        album = currentAlbum,
        handleIcon = iconState,
        albumDominantColor = dominantColor,
        songsList = songsList
    )
}

@Composable
fun AlbumBottomSheetItem(
    modifier: Modifier = Modifier,
    album: Album,
    handleIcon: ImageVector,
    albumDominantColor: Color,
    songsList: List<Song>
) {
    Column(modifier = modifier.backgroundGradient(albumDominantColor)) {
        AlbumBottomSheetHandle(icon = handleIcon)
        AlbumHeader(album = album)
        AlbumSongsList(songsList = songsList)
    }
}

@Composable
fun AlbumBottomSheetHandle(icon: ImageVector) {
    Crossfade(
        targetState = icon,
    ) { currentIcon ->
        Icon(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .alpha(0.6f),
            imageVector = currentIcon,
            contentDescription = "Swipe Action"
        )
    }
}

@Composable
fun AlbumHeader(
    modifier: Modifier = Modifier,
    album: Album
) {
    AlbumsItem(
        modifier = modifier.fillMaxWidth(),
        album = album,
        imageSize = Size(250f, 250f)
    )
}

@Composable
fun AlbumSongsList(
    modifier: Modifier = Modifier,
    songsList: List<Song>
) {
    SongsList(
        modifier = modifier.fillMaxWidth(),
        songsList = songsList
    )
    Spacer(modifier = Modifier.height(50.dp))
}