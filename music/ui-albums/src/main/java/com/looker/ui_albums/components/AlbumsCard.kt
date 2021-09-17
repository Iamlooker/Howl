package com.looker.ui_albums.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.looker.components.ComponentConstants.calculateItemSize
import com.looker.components.HowlImage
import com.looker.components.MaterialCard
import com.looker.components.WrappedText
import com.looker.components.rememberDominantColorState
import com.looker.domain_music.Album
import kotlinx.coroutines.launch

@Composable
fun AlbumsCard(
    modifier: Modifier = Modifier,
    album: Album,
    onClick: () -> Unit = {}
) {

    val context = LocalContext.current

    val cardWidth = remember {
        context.calculateItemSize(false, 2, 20.dp)
    }

    AlbumsCard(modifier.padding(10.dp), album, cardWidth, onClick)
}

@Composable
private fun AlbumsCard(
    modifier: Modifier = Modifier,
    album: Album,
    cardWidth: Dp,
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
            cardWidth = cardWidth,
            imageBackgroundColor = backgroundColor.color.copy(0.4f)
        )
    }
}

@Composable
fun AlbumsItem(
    modifier: Modifier = Modifier,
    album: Album,
    cardWidth: Dp,
    imageBackgroundColor: Color = MaterialTheme.colors.surface,
    imageShape: CornerBasedShape = MaterialTheme.shapes.medium,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        HowlImage(
            modifier = Modifier.size(cardWidth),
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