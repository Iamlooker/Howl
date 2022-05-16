package com.looker.domain_music

data class Song(
	val mediaId: String,
	val songUri: String,
	val name: String,
	val artistName: String,
	val albumName: String,
	val albumArt: String,
	val duration: Long = 0,
)

val emptySong = Song(
	mediaId = "",
	songUri = "",
	name = "",
	artistName = "",
	albumName = "",
	albumArt = ""
)