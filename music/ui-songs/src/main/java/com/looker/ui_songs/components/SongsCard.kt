package com.looker.ui_songs.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import com.looker.components.ComponentConstants.calculateItemSize
import com.looker.components.ItemCard
import com.looker.domain_music.Song

@Composable
fun SongsCard(modifier: Modifier = Modifier, song: Song, onClick: () -> Unit) {

    val context = LocalContext.current

    val cardHeight by context.calculateItemSize(true, 14)

    SongsCard(modifier, song, cardHeight, onClick)
}

@Composable
private fun SongsCard(
    modifier: Modifier = Modifier,
    song: Song,
    cardHeight: Dp,
    onClick: () -> Unit
) {

    ItemCard(
        modifier = modifier,
        imageUrl = song.albumArt,
        title = song.songName,
        subText = song.artistName,
        imageSize = cardHeight,
        onClick = onClick
    )
}