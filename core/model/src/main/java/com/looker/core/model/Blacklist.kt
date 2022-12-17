package com.looker.core.model

data class Blacklist(
	val songs: Set<String> = emptySet(),
	val albums: Set<String> = emptySet(),
	val songsFromAlbum: Set<String> = emptySet()
)
