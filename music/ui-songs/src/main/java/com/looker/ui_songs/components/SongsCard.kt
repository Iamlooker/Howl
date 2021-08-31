package com.looker.ui_songs.components

import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.looker.components.ComponentConstants.itemSize
import com.looker.components.HowlImage
import com.looker.components.MaterialCard
import com.looker.components.WrappedText
import com.looker.components.rememberDominantColorState
import com.looker.data_music.data.Song
import kotlinx.coroutines.launch

private val Long.artworkUri: Uri?
    get() = Uri.parse("content://media/external/audio/albumart/$this")

@Composable
fun SongsCard(modifier: Modifier = Modifier, song: Song) {

    val context = LocalContext.current

    val itemHeight = context.itemSize(true, 14)

    Box(modifier = modifier) {
        SongsCard(modifier = modifier, song = song, cardHeight = itemHeight)
    }
}

@Composable
private fun SongsCard(modifier: Modifier = Modifier, song: Song, cardHeight: Dp) {

    val backgroundColor = rememberDominantColorState()

    LaunchedEffect(song) {
        launch {
            backgroundColor.updateColorsFromImageUrl(song.albumId.artworkUri.toString())
        }
    }

    MaterialCard(
        modifier = modifier.padding(10.dp),
        rippleColor = backgroundColor.color.copy(0.2f),
        elevation = 0.dp
    ) {
        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = CenterVertically
        ) {
            SongItem(song = song, imageSize = cardHeight)
        }
    }
}

@Composable
fun SongItem(song: Song, imageSize: Dp) {
    HowlImage(
        data = song.albumId.artworkUri,
        modifier = Modifier.size(imageSize),
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
        WrappedText(text = songName)
        WrappedText(text = artistName, style = MaterialTheme.typography.caption)
    }
}