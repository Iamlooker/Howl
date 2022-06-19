package com.looker.core_model

data class Blacklist(
	val songs: Set<String> = emptySet(),
	val albums: Set<String> = emptySet(),
	val songsFromAlbum: Set<String> = emptySet()
)
