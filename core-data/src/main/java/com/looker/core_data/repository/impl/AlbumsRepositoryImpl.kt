package com.looker.core_data.repository.impl

import com.looker.core_data.repository.AlbumsRepository
import com.looker.core_database.dao.AlbumDao
import com.looker.core_database.model.AlbumEntity
import com.looker.core_database.model.asExternalModel
import com.looker.core_model.Album
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AlbumsRepositoryImpl @Inject constructor(
	private val albumDao: AlbumDao
) : AlbumsRepository {
	override fun getAlbumsStream(): Flow<List<Album>> =
		albumDao.getAlbumEntitiesStream()
			.map { it.map(AlbumEntity::asExternalModel) }

	override fun getAlbumStream(albumId: Long): Flow<Album> =
		albumDao.getAlbumEntityStream(albumId).map {
			it.asExternalModel()
		}
}