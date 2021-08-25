package com.looker.ui_songs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.looker.data_songs.data.Song
import com.looker.ui_songs.components.SongsCard

@Composable
fun Songs(viewModel: SongsViewModel = viewModel()) {

    SongsList(songsList = viewModel.getSongsList())
}

@Composable
fun SongsList(
    modifier: Modifier = Modifier,
    songsList: List<Song>
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
    ) {
        items(songsList) { song ->
            SongsCard(modifier = Modifier.fillMaxWidth(), song = song)
        }
    }
}