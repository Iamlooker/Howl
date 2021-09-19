package com.looker.domain_music

data class Song(
    val songUri: String,
    val albumId: Long,
    val genreId: Long,
    val songName: String = "No Name",
    val artistName: String = "No Name",
    val albumName: String = "No Name",
    val albumArt: String = "content://media/external/audio/albumart/$albumId",
    val duration: Long = 0
)

val emptySong: Song =
    Song(
        songUri = "",
        albumId = 0,
        genreId = 0,
        songName = "Placeholder Song",
        artistName = "Placeholder Artist",
        albumName = "Placeholder Album",
        albumArt = "",
        duration = 100000
    )