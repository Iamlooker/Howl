package com.looker.ui_player

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MoreHoriz
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.looker.components.ShapedIconButton
import com.looker.components.ToggleButton
import com.looker.ui_player.components.AlbumArtAndUtils
import com.looker.ui_player.components.SongText

@Composable
fun PlayerHeader(
    modifier: Modifier = Modifier,
    albumArt: String?,
    songName: String?,
    artistName: String?,
    onImageIcon: ImageVector,
    repeatIcon: ImageVector,
    toggled: Boolean,
    imageCorner: Int = 50,
    toggleAction: () -> Unit
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AlbumArtAndUtils(
            modifier = Modifier
                .fillMaxHeight(0.27f)
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            albumArt = albumArt,
            icon = onImageIcon,
            toggled = toggled,
            onToggle = toggleAction,
            albumArtCorner = imageCorner,
            contentDescription = "Play"
        ) {
            ToggleButton(
                icon = repeatIcon,
                toggled = toggled,
                onToggle = {},
                contentDescription = "Repeat"
            )
            ShapedIconButton(
                icon = Icons.Rounded.MoreHoriz,
                backgroundColor = MaterialTheme.colors.surface,
                contentDescription = "More"
            ) {}
        }
        SongText(songName = songName, artistName = artistName)
    }
}