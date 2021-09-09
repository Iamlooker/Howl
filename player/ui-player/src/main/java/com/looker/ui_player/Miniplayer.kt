package com.looker.ui_player

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.looker.components.HowlImage
import com.looker.components.WrappedText

@Composable
fun MiniPlayer(
    modifier: Modifier = Modifier,
    albumArt: Any,
    songName: String,
    artistName: String
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colors.background),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        MiniPlayerSongItem(
            albumArt = albumArt,
            songName = songName,
            artistName = artistName
        )
        IconButton(onClick = { }) {
            Icon(
                imageVector = Icons.Rounded.PlayArrow,
                contentDescription = "Play Icon"
            )
        }
    }
}

@Composable
fun MiniPlayerSongItem(
    modifier: Modifier = Modifier,
    albumArt: Any,
    songName: String,
    artistName: String
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        HowlImage(
            modifier = Modifier.size(50.dp),
            data = albumArt,
            shape = CircleShape
        )
        MiniPlayerSongText(songName = songName, artistName = artistName)
    }
}

@Composable
fun MiniPlayerSongText(
    modifier: Modifier = Modifier,
    songName: String,
    artistName: String
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start
    ) {
        WrappedText(text = songName)
        WrappedText(
            text = artistName,
            style = MaterialTheme.typography.caption
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MiniPlayerPreview() {
    MiniPlayer(
        songName = "Name",
        artistName = "Name",
        albumArt = R.drawable.error_image
    )
}