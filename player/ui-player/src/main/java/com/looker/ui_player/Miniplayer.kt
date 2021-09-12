package com.looker.ui_player

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.looker.ui_player.components.AlbumArtAndUtils
import com.looker.ui_player.components.SongText

@Composable
fun MiniPlayer(
    modifier: Modifier = Modifier,
    albumArt: Any,
    songName: String,
    artistName: String,
    icon: ImageVector,
    toggled: Boolean,
    toggleAction: () -> Unit
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AlbumArtAndUtils(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .size(400.dp, 250.dp),
            albumArt = albumArt,
            icon = icon,
            toggled = toggled,
            onToggle = toggleAction,
            contentDescription = "PlayToggle"
        )
        SongText(songName = songName, artistName = artistName)
    }
}