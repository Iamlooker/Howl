package com.looker.core_model

data class Album(
	val albumId: Long = 0,
	val name: String = "",
	val artist: String = "",
	val albumArt: String = "",
	val numberOfSongs: Int = 0
)