package com.looker.ui_songs.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.looker.components.*
import com.looker.domain_music.Song
import kotlinx.coroutines.launch

@Composable
fun SongsCard(modifier: Modifier = Modifier, song: Song, onClick: () -> Unit) {
    Box(modifier = modifier) {
        SongsCard(modifier = modifier, song = song, count = 14f, onClick = onClick)
    }
}

@Composable
private fun SongsCard(
    modifier: Modifier = Modifier,
    song: Song,
    count: Float,
    onClick: () -> Unit
) {

    val backgroundColor = rememberDominantColorState()

    LaunchedEffect(song) {
        launch {
            backgroundColor.updateColorsFromImageUrl(song.albumArt)
        }
    }

    MaterialCard(
        modifier = modifier.padding(10.dp),
        elevation = 0.dp,
        onClick = onClick
    ) {
        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = CenterVertically
        ) {
            SongItem(song = song, rowCounts = count)
        }
    }
}

@Composable
fun SongItem(song: Song, rowCounts: Float) {
    HowlImage(
        data = song.albumArt,
        modifier = Modifier.itemSize(true, rowCounts),
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