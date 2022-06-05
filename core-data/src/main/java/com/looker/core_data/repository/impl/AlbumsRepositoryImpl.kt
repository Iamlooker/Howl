package com.looker.core_data.repository.impl

import android.content.Context
import com.looker.core_data.repository.AlbumsRepository
import com.looker.core_database.dao.AlbumDao
import com.looker.core_database.model.AlbumEntity
import com.looker.core_database.model.asEntity
import com.looker.core_database.model.asExternalModel
import com.looker.core_model.Album
import com.looker.data_music.AlbumsData
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AlbumsRepositoryImpl @Inject constructor(
	@ApplicationContext
	private val appContext: Context,
	private val albumDao: AlbumDao
) : AlbumsRepository {
	override fun getAlbumsStream(): Flow<List<Album>> =
		albumDao.getAlbumEntitiesStream()
			.map { it.map(AlbumEntity::asExternalModel) }

	override fun getAlbumStream(albumId: Long): Flow<Album> =
		albumDao.getAlbumEntityStream(albumId).map {
			it.asExternalModel()
		}

	override suspend fun syncData(): Boolean {
		val albums = AlbumsData(appContext).createAlbumsList().map { it.asEntity() }
		albumDao.insertOrIgnoreAlbums(albums)
		return true
	}
}