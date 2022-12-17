package com.looker.core.model

data class Song(
	val mediaId: String = "",
	val albumId: Long = 0,
	val name: String = "",
	val artist: String = "",
	val album: String = "",
	val albumArt: String = "",
	val pathUri: String = "",
	val duration: Long = 0L
)