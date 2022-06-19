package com.looker.core_data.repository

import com.looker.core_model.Blacklist
import kotlinx.coroutines.flow.Flow

interface BlacklistsRepository {
	fun getBlacklistSongs(): Flow<List<Blacklist>>
	suspend fun addToBlacklist(blacklist: Blacklist)
	suspend fun updateBlacklist(blacklists: List<Blacklist>)
	suspend fun allowSongs(ids: Set<String>)
	suspend fun allowAlbums(ids: Set<String>)
	suspend fun allowSongsFromAlbum(albumId: Set<String>)
}