package com.looker.ui_player.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import com.looker.components.HowlImage
import com.looker.components.ToggleButton
import com.looker.components.ext.rippleClick
import com.looker.components.tweenAnimation

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AlbumArtAndUtils(
    modifier: Modifier = Modifier,
    albumArt: String?,
    icon: ImageVector,
    toggled: Boolean,
    contentDescription: String?,
    albumArtCorner: Int,
    onToggle: () -> Unit,
    overlayItems: @Composable RowScope.() -> Unit
) {
    var overlayVisible by remember { mutableStateOf(false) }

    Box {
        AlbumArt(
            modifier = modifier.rippleClick { overlayVisible = !overlayVisible },
            albumArt = albumArt,
            corner = albumArtCorner
        )
        AnimatedVisibility(
            visible = overlayVisible,
            enter = fadeIn(animationSpec = tweenAnimation()),
            exit = fadeOut(animationSpec = tweenAnimation())
        ) {
            AlbumArtOverlay(
                modifier = modifier.fillMaxWidth(),
                corner = albumArtCorner,
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
    corner: Int,
    onClick: () -> Unit,
    content: @Composable RowScope.() -> Unit
) {
    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(corner))
            .rippleClick(onClick = onClick),
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
    corner: Int = 50
) {
    HowlImage(
        modifier = modifier.fillMaxWidth(),
        data = albumArt,
        shape = RoundedCornerShape(corner)
    )
}