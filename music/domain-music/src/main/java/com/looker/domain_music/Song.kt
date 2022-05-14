package com.looker.domain_music

data class Song(
	val mediaId: String,
	val songUri: String,
	val songName: String?,
	val artistName: String?,
	val albumName: String?,
	val albumArt: String?,
	val duration: Long = 0,
)

val emptySong = Song(
	mediaId = "",
	songUri = "",
	songName = null,
	artistName = null,
	albumName = null,
	albumArt = null
)