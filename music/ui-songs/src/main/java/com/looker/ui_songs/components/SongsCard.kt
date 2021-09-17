package com.looker.ui_songs.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.looker.components.ComponentConstants.calculateItemSize
import com.looker.components.HowlImage
import com.looker.components.MaterialCard
import com.looker.components.WrappedText
import com.looker.domain_music.Song

@Composable
fun SongsCard(modifier: Modifier = Modifier, song: Song, onClick: () -> Unit) {

    val context = LocalContext.current

    val cardHeight = remember { context.calculateItemSize(true, 14) }

    SongsCard(modifier.padding(8.dp), song, cardHeight, onClick)
}

@Composable
private fun SongsCard(
    modifier: Modifier = Modifier,
    song: Song,
    cardHeight: Dp,
    onClick: () -> Unit
) {
    MaterialCard(
        modifier = modifier,
        onClick = onClick
    ) {
        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = CenterVertically
        ) {
            SongItem(song, cardHeight)
        }
    }
}

@Composable
fun SongItem(song: Song, cardHeight: Dp) {
    HowlImage(
        modifier = Modifier.size(cardHeight),
        data = song.albumArt,
        shape = MaterialTheme.shapes.medium
    )
    Spacer(modifier = Modifier.width(10.dp))
    SongItemText(
        songName = song.songName,
        artistName = song.artistName
    )
}

@Composable
fun SongItemText(modifier: Modifier = Modifier, songName: String, artistName: String) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.Start
    ) {
        WrappedText(
            text = songName,
            fontWeight = FontWeight.SemiBold
        )
        WrappedText(
            text = artistName,
            style = MaterialTheme.typography.caption
        )
    }
}