package com.looker.ui_player.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import coil.ImageLoader
import com.looker.components.HowlImage
import com.looker.components.ToggleButton

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AlbumArtAndUtils(
    modifier: Modifier = Modifier,
    albumArt: String?,
    imageLoader: ImageLoader,
    icon: ImageVector,
    toggled: Boolean,
    onToggle: () -> Unit,
    contentDescription: String?,
    overlayItems: @Composable () -> Unit = {}
) {
    var overlayVisible by remember { mutableStateOf(false) }

    Box {
        HowlImage(
            modifier = modifier
                .fillMaxWidth()
                .clickable(
                    onClick = { overlayVisible = !overlayVisible },
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ),
            data = albumArt,
            imageLoader = imageLoader,
            shape = CircleShape
        )
        AnimatedVisibility(
            visible = overlayVisible,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            AlbumArtOverlay(
                modifier = modifier.fillMaxWidth(),
                onClick = { overlayVisible = !overlayVisible },
                content = overlayItems
            )
        }
        ToggleButton(
            modifier = Modifier.align(Alignment.BottomEnd),
            icon = icon,
            toggled = toggled,
            onToggle = onToggle,
            contentDescription = contentDescription
        )
    }
}

@Composable
fun AlbumArtOverlay(
    modifier: Modifier = Modifier,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    onClick: () -> Unit,
    content: @Composable () -> Unit
) {
    Surface(
        modifier = modifier
            .clip(CircleShape)
            .clickable(
                onClick = onClick,
                indication = null,
                interactionSource = interactionSource
            ),
        color = MaterialTheme.colors.onBackground.copy(0.4f),
        content = content
    )
}