package com.looker.ui_albums

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import com.looker.components.ComponentConstants.DefaultCrossfadeDuration
import com.looker.components.ComponentConstants.tweenAnimation
import com.looker.components.HandleIcon
import com.looker.components.ItemCard
import com.looker.components.backgroundGradient
import com.looker.domain_music.Album
import com.looker.domain_music.Song
import com.looker.ui_songs.SongsList

@Composable
fun AlbumsBottomSheetContent(
    modifier: Modifier = Modifier,
    currentAlbum: Album,
    imageLoader: ImageLoader,
    handleIcon: Float,
    songsList: List<Song>,
    dominantColor: Color = MaterialTheme.colors.surface
) {
    AlbumBottomSheetItem(
        modifier = modifier,
        album = currentAlbum,
        imageLoader = imageLoader,
        handleIcon = handleIcon,
        albumDominantColor = dominantColor,
        songsList = songsList
    )
}

@Composable
fun AlbumBottomSheetItem(
    modifier: Modifier = Modifier,
    album: Album,
    imageLoader: ImageLoader,
    handleIcon: Float,
    albumDominantColor: Color,
    songsList: List<Song>
) {
    Column(modifier = modifier.backgroundGradient(albumDominantColor)) {
        HandleIcon(angle = handleIcon, backgroundColor = Color.Transparent)
        AlbumHeader(album = album, imageLoader = imageLoader)
        AlbumSongsList(songsList = songsList, imageLoader = imageLoader)
    }
}

@Composable
fun AlbumHeader(
    modifier: Modifier = Modifier,
    imageLoader: ImageLoader,
    album: Album
) {
    Crossfade(targetState = album, animationSpec = tweenAnimation(DefaultCrossfadeDuration)) {
        ItemCard(
            modifier = modifier.fillMaxWidth(),
            imageUrl = it.albumArt,
            imageLoader = imageLoader,
            title = it.albumName,
            subText = it.artistName,
            imageSize = 250.dp
        )
    }
}

@Composable
fun AlbumSongsList(
    modifier: Modifier = Modifier,
    imageLoader: ImageLoader,
    songsList: List<Song>
) {
    Crossfade(targetState = songsList, animationSpec = tweenAnimation(DefaultCrossfadeDuration)) {
        SongsList(
            modifier = modifier.fillMaxWidth(),
            imageLoader = imageLoader,
            songsList = it
        )
    }
    Spacer(modifier = Modifier.height(50.dp))
}