package com.looker.data_music.data

import android.net.Uri
import androidx.compose.runtime.Immutable

@Immutable
data class Song(
    val songUri: Uri,
    val albumId: Long,
    val songName: String = "No Name",
    val artistName: String = "No Name",
    val albumName: String = "No Name",
    val duration: Long
)