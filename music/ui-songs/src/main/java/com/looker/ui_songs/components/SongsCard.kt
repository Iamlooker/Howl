package com.looker.ui_songs.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import coil.ImageLoader
import com.looker.components.ItemCard
import com.looker.domain_music.Song

@Composable
fun SongsCard(
    modifier: Modifier = Modifier,
    imageLoader: ImageLoader,
    song: Song,
    cardHeight: Dp,
    onClick: () -> Unit
) {
    ItemCard(
        modifier = modifier,
        imageUrl = song.albumArt,
        imageLoader = imageLoader,
        title = song.songName,
        subText = song.artistName,
        imageSize = cardHeight,
        onClick = onClick
    )
}