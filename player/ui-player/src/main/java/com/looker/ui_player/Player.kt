package com.looker.ui_player

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.insets.statusBarsPadding
import com.looker.components.HowlSurface
import com.looker.ui_player.components.AlbumArtAndUtils
import com.looker.ui_player.components.PlaybackControls
import com.looker.ui_player.components.SongText

@Composable
fun Player(
    modifier: Modifier = Modifier,
    viewModel: PlayerViewModel = viewModel()
) {

    val progress = remember { viewModel.progressValue }
    val shuffle = remember { viewModel.shuffle }

    HowlSurface(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {
        Column(
            modifier = modifier,
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AlbumArtAndUtils(
                modifier = Modifier.size(400.dp, 300.dp),
                albumArt = viewModel.albumArt,
                shuffle = shuffle.value,
                onShuffle = { viewModel.onShuffle() }
            )
            SongText(songName = viewModel.songName, artistName = viewModel.artistName)
            Spacer(modifier = Modifier.height(50.dp))
            PlaybackControls(
                modifier = Modifier.fillMaxWidth(),
                progressValue = progress.value,
                onSeek = { progress.value = it },
                playIcon = viewModel.playIcon,
                onPlayPause = { viewModel.onPlayPause() }
            )
        }
    }
}