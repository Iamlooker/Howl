package com.looker.ui_albums.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.looker.components.HowlImage
import com.looker.components.ItemCardText

@Composable
fun AlbumsDetailsItem(
    modifier: Modifier = Modifier,
    albumArt: String?,
    albumName: String?,
    artistName: String?
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        DetailsArt(albumArt = albumArt)
        DetailsText(albumName = albumName, artistName = artistName)
        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Composable
fun DetailsArt(
    modifier: Modifier = Modifier,
    albumArt: String?
) {
    HowlImage(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight(0.4f)
            .padding(20.dp),
        data = albumArt,
        shape = MaterialTheme.shapes.large
    )
}

@Composable
fun DetailsText(
    albumName: String?,
    artistName: String?
) {
    ItemCardText(
        title = albumName,
        subText = artistName,
        titleTextStyle = MaterialTheme.typography.h5,
        subTextTextStyle = MaterialTheme.typography.body1,
        itemTextAlignment = Alignment.CenterHorizontally
    )
}