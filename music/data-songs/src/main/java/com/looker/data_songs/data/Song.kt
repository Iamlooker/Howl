package com.looker.data_songs.data

import android.net.Uri

data class Song(
    val songUri: Uri,
    val albumId: Long,
    val songName: String,
    val artistName: String,
    val albumName: String,
    val duration: Long
)