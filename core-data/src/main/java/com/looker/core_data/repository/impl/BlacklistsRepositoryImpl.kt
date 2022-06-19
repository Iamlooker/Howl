package com.looker.core_data.repository.impl

import com.looker.core_data.repository.BlacklistsRepository
import com.looker.core_database.dao.BlacklistDao
import com.looker.core_database.model.BlacklistEntity
import com.looker.core_database.model.asEntity
import com.looker.core_database.model.asExternalModel
import com.looker.core_model.Blacklist
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class BlacklistsRepositoryImpl @Inject constructor(
	private val blacklistDao: BlacklistDao
) : BlacklistsRepository {
	override fun getBlacklistSongs(): Flow<List<Blacklist>> =
		blacklistDao.getBlacklistEntitiesStream().map { it.map(BlacklistEntity::asExternalModel) }

	override suspend fun addToBlacklist(blacklist: Blacklist) {
		blacklistDao.addBlacklist(blacklist.asEntity())
	}

	override suspend fun updateBlacklist(blacklists: List<Blacklist>) {
		blacklistDao.updateBlacklist(blacklists.map(Blacklist::asEntity))
	}

	override suspend fun allowSongs(ids: Set<String>) {
		blacklistDao.allowSongs(ids)
	}

	override suspend fun allowAlbums(ids: Set<String>) {
		blacklistDao.allowAlbum(ids)
	}

	override suspend fun allowSongsFromAlbum(albumId: Set<String>) {
		blacklistDao.allowAlbumIds(albumId)
	}
}