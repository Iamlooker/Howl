package com.looker.ui_albums.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.looker.components.*
import com.looker.components.ComponentConstants.tweenAnimation
import com.looker.domain_music.Album
import kotlinx.coroutines.launch

@Composable
fun AlbumsCard(
    modifier: Modifier = Modifier,
    album: Album,
    onClick: () -> Unit = {}
) {
    AlbumsCard(modifier.padding(10.dp), album, 2f, onClick)
}

@Composable
private fun AlbumsCard(
    modifier: Modifier = Modifier,
    album: Album,
    count: Float,
    onClick: () -> Unit
) {
    val backgroundColor = rememberDominantColorState()

    LaunchedEffect(album) {
        launch {
            backgroundColor.updateColorsFromImageUrl(album.albumArt)
        }
    }

    MaterialCard(
        modifier = modifier,
        elevation = 0.dp,
        backgroundColor = backgroundColor.color.copy(0.4f),
        rippleColor = backgroundColor.color,
        onClick = onClick
    ) {
        AlbumsItem(
            album = album,
            columnCount = count,
            imageBackgroundColor = backgroundColor.color.copy(0.4f)
        )
    }
}

@Composable
fun AlbumsItem(
    modifier: Modifier = Modifier,
    album: Album,
    columnCount: Float,
    imageBackgroundColor: Color = MaterialTheme.colors.surface,
    imageShape: CornerBasedShape = MaterialTheme.shapes.medium,
) {

    var defaultAlpha by remember {
        mutableStateOf(0f)
    }

    LaunchedEffect(defaultAlpha) {
        launch { defaultAlpha = 1f }
    }

    val fadeIn by animateFloatAsState(
        targetValue = defaultAlpha,
        animationSpec = tweenAnimation()
    )

    Column(
        modifier = modifier
            .wrapContentSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        HowlImage(
            modifier = Modifier
                .alpha(fadeIn)
                .itemSize(false, columnCount, 20.dp),
            data = album.albumArt,
            imageFillerColor = imageBackgroundColor,
            shape = imageShape,
        )
        AlbumsItemText(
            modifier = Modifier.padding(horizontal = 8.dp),
            albumName = album.albumName,
            artistName = album.artistName
        )
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
fun AlbumsItemText(
    modifier: Modifier = Modifier,
    albumName: String,
    artistName: String,
    textColor: Color = MaterialTheme.colors.onBackground
) {
    WrappedText(
        modifier = modifier,
        text = albumName,
        textAlign = TextAlign.Center,
        textColor = textColor
    )
    WrappedText(
        modifier = modifier,
        text = artistName,
        style = MaterialTheme.typography.body2,
        textAlign = TextAlign.Center,
        textColor = textColor
    )
}