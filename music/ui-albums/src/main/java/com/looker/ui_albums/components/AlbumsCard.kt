package com.looker.ui_albums.components

import android.net.Uri
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.TweenSpec
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.looker.components.ComponentConstants.itemSize
import com.looker.components.HowlImage
import com.looker.components.MaterialCard
import com.looker.components.WrappedText
import com.looker.components.rememberDominantColorState
import com.looker.data_albums.data.Album
import com.looker.ui_albums.components.AlbumsExtensions.artworkUri
import kotlinx.coroutines.launch

object AlbumsExtensions {

    val Long.artworkUri: Uri?
        get() = Uri.parse("content://media/external/audio/albumart/$this")
}

@Composable
fun AlbumsCard(
    modifier: Modifier = Modifier,
    album: Album
) {
    val cardWidth = LocalContext.current.itemSize(false, 2, 20.dp)
    AlbumsCard(
        modifier.padding(10.dp), album, cardWidth
    )
}

@Composable
private fun AlbumsCard(
    modifier: Modifier = Modifier,
    album: Album,
    cardWidth: Dp
) {
    val backgroundColor = rememberDominantColorState()

    val albumArtUri = album.albumId.artworkUri

    LaunchedEffect(albumArtUri) {
        launch {
            backgroundColor.updateColorsFromImageUrl(album.albumId.artworkUri.toString())
        }
    }

    val animatedColor by animateColorAsState(
        targetValue = backgroundColor.color.copy(0.3f),
        animationSpec = TweenSpec(
            durationMillis = 500
        )
    )

    MaterialCard(
        modifier = modifier,
        shape = MaterialTheme.shapes.large,
        backgroundColor = animatedColor,
        elevation = 0.dp
    ) {
        AlbumsItem(
            album = album,
            imageSize = cardWidth
        )
    }
}

@Composable
fun AlbumsItem(
    modifier: Modifier = Modifier,
    album: Album,
    imageSize: Dp
) {
    Column(
        modifier = modifier
            .wrapContentSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        HowlImage(
            data = album.albumId.artworkUri,
            modifier = Modifier.size(imageSize)
        )
        AlbumsItemText(
            modifier = Modifier.padding(horizontal = 8.dp),
            album = album
        )
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
fun AlbumsItemText(
    modifier: Modifier = Modifier,
    album: Album,
    textColor: Color = MaterialTheme.colors.onBackground
) {
    WrappedText(
        modifier = modifier,
        text = album.albumName,
        textAlign = TextAlign.Center,
        textColor = textColor
    )
    WrappedText(
        modifier = modifier,
        text = album.artistName,
        style = MaterialTheme.typography.body2,
        textAlign = TextAlign.Center,
        textColor = textColor
    )
}