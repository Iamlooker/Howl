package com.looker.ui_genre.components

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.looker.components.ComponentConstants.calculateItemSize
import com.looker.components.ItemCard
import com.looker.domain_music.Genre

@Composable
fun GenresCard(modifier: Modifier = Modifier, genre: Genre, onClick: () -> Unit) {

    val context = LocalContext.current

    val cardWidth by context.calculateItemSize(false, 2, 16.dp)

    GenresCard(modifier, genre, cardWidth, onClick)
}


// TODO: 9/21/2021 Add API for getting suitable Genre Art
@Composable
private fun GenresCard(
    modifier: Modifier = Modifier,
    genre: Genre,
    cardWidth: Dp,
    onClick: () -> Unit
) {
    ItemCard(
        modifier = modifier,
        imageShape = CircleShape,
        title = genre.genreName,
        subText = "${genre.count} Songs",
        imageSize = cardWidth,
        onClick = onClick
    )
}