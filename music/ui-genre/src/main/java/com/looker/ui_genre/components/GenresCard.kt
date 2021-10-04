package com.looker.ui_genre.components

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import com.looker.components.ItemCard
import com.looker.domain_music.Genre

@Composable
fun GenresCard(
    modifier: Modifier = Modifier,
    genre: Genre,
    imageLoader: ImageLoader,
    onClick: () -> Unit
) {
    val cardWidth = with(LocalConfiguration.current) { screenWidthDp.dp / 2 - 16.dp }

    GenresCard(modifier, genre, imageLoader, cardWidth, onClick)
}

@Composable
private fun GenresCard(
    modifier: Modifier = Modifier,
    genre: Genre,
    imageLoader: ImageLoader,
    cardWidth: Dp,
    onClick: () -> Unit
) {
    ItemCard(
        modifier = modifier,
        imageShape = CircleShape,
        imageUrl = null,
        imageLoader = imageLoader,
        title = genre.genreName,
        subText = "${genre.count} Songs",
        imageSize = cardWidth,
        onClick = onClick
    )
}