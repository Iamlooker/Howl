package com.looker.domain_music

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "songs")
data class Song(
    @PrimaryKey
    val songUri: String,
    val albumId: Long,
    val genreId: Long,
    val songName: String?,
    val artistName: String?,
    val albumName: String?,
    val albumArt: String,
    val browsable: Boolean = true,
    val duration: Long = 0,
)

val emptySong = Song(
    songUri = "",
    albumId = 0,
    genreId = 0,
    songName = null,
    artistName = null,
    albumName = null,
    albumArt = ""
)