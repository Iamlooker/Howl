package com.looker.ui_genre.components

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.looker.components.LargeCard
import com.looker.domain_music.Genre

@Composable
fun GenresCard(
    modifier: Modifier = Modifier,
    genre: Genre,
    onClick: () -> Unit
) {
    val cardWidth = with(LocalConfiguration.current) { screenWidthDp.dp / 2 - 16.dp }

    GenresCard(modifier, genre, cardWidth, onClick)
}

@Composable
private fun GenresCard(
    modifier: Modifier = Modifier,
    genre: Genre,
    cardWidth: Dp,
    onClick: () -> Unit
) {
    LargeCard(
        modifier = modifier,
        imageShape = CircleShape,
        imageUrl = null,
        title = genre.genreName,
        subText = "${genre.count} Songs",
        imageSize = cardWidth,
        onClick = onClick
    )
}