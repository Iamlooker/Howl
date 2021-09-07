package com.looker.data_music.data

import androidx.compose.runtime.Immutable

@Immutable
data class Album(
    val albumId: Long,
    val albumName: String = "No Name",
    val artistName: String = "No Artist",
)