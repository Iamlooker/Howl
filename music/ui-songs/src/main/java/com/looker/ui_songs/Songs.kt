package com.looker.ui_songs

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.ImageLoader
import com.looker.components.HowlSurface
import com.looker.data_music.data.SongsRepository
import com.looker.domain_music.Song
import com.looker.ui_songs.components.SongsCard
import kotlinx.coroutines.launch

@Composable
fun Songs(imageLoader: ImageLoader, onSongClick: (Song) -> Unit) {
    Songs(modifier = Modifier.fillMaxSize(), imageLoader = imageLoader, onSongClick = onSongClick)
}

@Composable
private fun Songs(
    modifier: Modifier = Modifier,
    imageLoader: ImageLoader,
    viewModel: SongsViewModel = viewModel(factory = SongsViewModelFactory(SongsRepository())),
    onSongClick: (Song) -> Unit
) {
    val context = LocalContext.current

    val songsList by viewModel.songsList.observeAsState(initial = listOf())

    LaunchedEffect(songsList) { launch { viewModel.getSongsList(context) } }

    HowlSurface(modifier = modifier) {
        SongsList(imageLoader = imageLoader, songsList = songsList, onSongClick = onSongClick)
    }
}

@Composable
fun SongsList(
    modifier: Modifier = Modifier,
    imageLoader: ImageLoader,
    songsList: List<Song>,
    onSongClick: (Song) -> Unit = {}
) {
    val height = with(LocalConfiguration.current) { screenHeightDp.dp / 14 }

    LazyColumn(modifier = modifier) {
        items(songsList) { song ->
            SongsCard(Modifier.fillMaxWidth(), imageLoader, song, height) { onSongClick(song) }
        }
    }
}
