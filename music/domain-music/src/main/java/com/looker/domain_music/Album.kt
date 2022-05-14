package com.looker.domain_music

data class Album(
	val albumId: String,
	val albumName: String?,
	val artistName: String?,
	val albumArt: String?,
)

val emptyAlbum = Album("", null, null, null)