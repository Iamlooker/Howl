package com.looker.ui_songs

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.looker.domain_music.Song
import com.looker.ui_songs.components.SongsCard

@Composable
fun Songs(
    songsList: List<Song>,
    onSongClick: (Int) -> Unit
) {
    SongsList(songsList = songsList, onSongClick = onSongClick)
}

@Composable
fun SongsList(
    modifier: Modifier = Modifier,
    songsList: List<Song>,
    onSongClick: (Int) -> Unit = {}
) {
    val height = with(LocalConfiguration.current) { screenHeightDp.dp / 14 }
    LazyColumn(modifier = modifier) {
        itemsIndexed(songsList) { index, song ->
            SongsCard(Modifier.fillMaxWidth(), song, height) { onSongClick(index) }
        }
    }
}
