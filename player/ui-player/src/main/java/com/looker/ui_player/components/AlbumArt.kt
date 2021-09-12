package com.looker.ui_player.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.looker.components.HowlImage
import com.looker.components.ToggleButton

@Composable
fun AlbumArtAndUtils(
    modifier: Modifier = Modifier,
    albumArt: Any,
    icon: ImageVector,
    toggled: Boolean,
    onToggle: () -> Unit,
    contentDescription: String?
) {
    Box {
        HowlImage(
            modifier = modifier,
            data = albumArt,
            shape = CircleShape
        )
        ToggleButton(
            modifier = Modifier.align(Alignment.BottomEnd),
            icon = icon,
            toggled = toggled,
            onToggle = onToggle,
            contentDescription = contentDescription
        )
    }
}