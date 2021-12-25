package com.looker.data_music.db

import com.looker.domain_music.Song

class SongsStore(
	private val songsDao: SongsDao,
) {
	suspend fun addSongs(songs: List<Song>) = songsDao.insertAllSongs(songs)

	suspend fun isEmpty(): Boolean = songsDao.count() == 0
}