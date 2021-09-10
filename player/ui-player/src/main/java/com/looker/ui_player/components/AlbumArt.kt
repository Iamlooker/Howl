package com.looker.ui_player.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.looker.components.HowlImage

@Composable
fun AlbumArtAndUtils(
    modifier: Modifier = Modifier,
    albumArt: Any,
    shuffle: Boolean,
    onShuffle: () -> Unit
) {
    Box {
        HowlImage(
            modifier = modifier,
            data = albumArt,
            shape = CircleShape
        )
        ShuffleButton(
            modifier = Modifier.align(Alignment.BottomEnd),
            shuffle = shuffle,
            onShuffle = onShuffle
        )
    }
}