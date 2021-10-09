package com.looker.domain_music

data class Album(
    val albumId: Long,
    val albumName: String?,
    val artistName: String?,
    val albumArt: String
)

val emptyAlbum = Album(0, null, null, "")