package com.looker.data_songs.data

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Song(
    @PrimaryKey
    val songUri: Uri,
    val albumId: Long,
    val songName: String,
    val artistName: String,
    val albumName: String,
    val duration: Long
)