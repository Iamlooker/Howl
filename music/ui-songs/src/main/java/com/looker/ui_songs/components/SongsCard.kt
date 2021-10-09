package com.looker.ui_songs.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import com.looker.components.SmallCard
import com.looker.domain_music.Song

@Composable
fun SongsCard(
    modifier: Modifier = Modifier,
    song: Song,
    cardHeight: Dp,
    onClick: () -> Unit
) {
    SmallCard(
        modifier = modifier,
        imageUrl = song.albumArt,
        title = song.songName,
        subText = song.artistName,
        imageSize = cardHeight,
        onClick = onClick
    )
}