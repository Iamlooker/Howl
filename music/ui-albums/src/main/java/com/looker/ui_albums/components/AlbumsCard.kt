package com.looker.ui_albums.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import com.looker.components.ComponentConstants.calculateItemSize
import com.looker.components.ItemCard
import com.looker.components.rememberDominantColorState
import com.looker.domain_music.Album
import kotlinx.coroutines.launch

@Composable
fun AlbumsCard(
    modifier: Modifier = Modifier,
    imageLoader: ImageLoader,
    album: Album,
    onClick: () -> Unit
) {

    val context = LocalContext.current

    val cardWidth by context.calculateItemSize(false, 2, 16.dp)

    AlbumsCard(modifier, imageLoader, album, cardWidth, onClick)
}

@Composable
private fun AlbumsCard(
    modifier: Modifier = Modifier,
    imageLoader: ImageLoader,
    album: Album,
    cardWidth: Dp,
    onClick: () -> Unit
) {
    val backgroundColor = rememberDominantColorState()

    LaunchedEffect(album) {
        launch {
            backgroundColor.updateColorsFromImageUrl(album.albumArt)
        }
    }

    ItemCard(
        modifier = modifier,
        imageUrl = album.albumArt,
        imageLoader = imageLoader,
        title = album.albumName,
        subText = album.artistName,
        cardColor = backgroundColor.color.copy(0.4f),
        rippleColor = backgroundColor.color,
        imageSize = cardWidth,
        onClick = onClick
    )
}