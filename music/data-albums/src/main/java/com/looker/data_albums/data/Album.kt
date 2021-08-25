package com.looker.data_albums.data

data class Album(
    val albumId: Long,
    val albumName: String = "No Name",
    val artistName: String = "No Artist",
)