package com.looker.domain_music

data class Album(
    val albumId: Long,
    val albumName: String = "No Name",
    val artistName: String = "No Artist",
    val albumArt: String = "content://media/external/audio/albumart/$albumId"
)