package com.looker.ui_player

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.looker.ui_player.components.MiniPlayerItem

@Composable
fun MiniPlayer(
    modifier: Modifier = Modifier,
    albumArt: Any,
    songName: String,
    artistName: String
) {
    MiniPlayerItem(modifier, albumArt, songName, artistName)
}