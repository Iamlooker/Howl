package com.looker.data_music.data

data class Album(
    val albumId: Long,
    val albumName: String = "No Name",
    val artistName: String = "No Artist",
)