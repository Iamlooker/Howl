package com.looker.ui_player.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.looker.components.HowlImage
import com.looker.components.ToggleButton
import com.looker.components.ext.rippleClick

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
            modifier = modifier,
            albumArt = albumArt,
            shape = RoundedCornerShape(albumArtCorner),
            overlayVisible = overlayVisible,
            onClick = { overlayVisible = !overlayVisible },
            overlayItems = overlayItems
        )

        ToggleButton(
            modifier = Modifier
                .padding(horizontal = 24.dp, vertical = 8.dp)
                .align(Alignment.BottomEnd),
            icon = icon,
            toggled = toggled,
            shape = MaterialTheme.shapes.medium,
            contentPadding = PaddingValues(vertical = 16.dp),
            onToggle = onToggle,
            contentDescription = contentDescription
        )
    }
}

@Composable
fun AlbumArt(
    modifier: Modifier = Modifier,
    albumArt: String?,
    shape: CornerBasedShape,
    overlayVisible: Boolean,
    onClick: () -> Unit,
    overlayItems: @Composable (RowScope.() -> Unit)
) {
    val overlay by animateFloatAsState(
        targetValue = if (overlayVisible) 1f else 0f,
        animationSpec = spring(
            stiffness = Spring.StiffnessLow,
            dampingRatio = Spring.DampingRatioLowBouncy
        )
    )

    Box(modifier = modifier.clip(shape)) {
        HowlImage(
            modifier = Modifier
                .matchParentSize()
                .rippleClick(onClick = onClick),
            data = albumArt,
            shape = shape
        )
        AlbumArtOverlay(
            modifier = Modifier
                .matchParentSize()
                .scale(overlay),
            shape = shape,
            onClick = onClick,
            content = overlayItems
        )
    }
}

@Composable
fun AlbumArtOverlay(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    shape: CornerBasedShape,
    content: @Composable RowScope.() -> Unit
) {
    Surface(
        modifier = modifier
            .clip(shape)
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