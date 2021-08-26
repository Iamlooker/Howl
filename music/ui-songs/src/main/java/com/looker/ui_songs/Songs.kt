package com.looker.ui_songs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.rememberInsetsPaddingValues
import com.looker.data_songs.data.Song
import com.looker.ui_songs.components.SongsCard

@Composable
fun Songs(viewModel: SongsViewModel = viewModel()) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background
    ) { SongsList(songsList = viewModel.getSongsList()) }
}

@Composable
fun SongsList(
    modifier: Modifier = Modifier,
    songsList: List<Song>
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        contentPadding = rememberInsetsPaddingValues(
            insets = LocalWindowInsets.current.systemBars,
            applyTop = true
        )
    ) {
        items(songsList) { song ->
            SongsCard(modifier = Modifier.fillMaxWidth(), song = song)
        }
    }
}