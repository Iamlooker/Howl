package com.looker.ui_player.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import com.looker.components.HowlImage
import com.looker.components.ToggleButton
import com.looker.components.tweenAnimation

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AlbumArtAndUtils(
    modifier: Modifier = Modifier,
    albumArt: String?,
    icon: ImageVector,
    toggled: Boolean,
    onToggle: () -> Unit,
    contentDescription: String?,
    overlayItems: @Composable RowScope.() -> Unit
) {
    var overlayVisible by remember { mutableStateOf(false) }

    Box {
        AlbumArt(
            modifier = modifier
                .clickable(
                    onClick = { overlayVisible = !overlayVisible },
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ),
            albumArt = albumArt,
        )
        AnimatedVisibility(
            visible = overlayVisible,
            enter = fadeIn(animationSpec = tweenAnimation()),
            exit = fadeOut(animationSpec = tweenAnimation())
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
    content: @Composable RowScope.() -> Unit
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
        content = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly,
                content = content
            )
        }
    )
}

@Composable
fun AlbumArt(
    modifier: Modifier = Modifier,
    albumArt: String?,
) {
    HowlImage(
        modifier = modifier.fillMaxWidth(),
        data = albumArt,
        shape = CircleShape
    )
}