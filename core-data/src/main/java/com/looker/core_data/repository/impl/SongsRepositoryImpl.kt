package com.looker.core_data.repository.impl

import com.looker.core_data.repository.SongsRepository
import com.looker.core_database.dao.AlbumDao
import com.looker.core_database.dao.SongDao
import com.looker.core_database.model.SongEntity
import com.looker.core_database.model.asExternalModel
import com.looker.core_model.Song
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SongsRepositoryImpl @Inject constructor(
	private val songDao: SongDao,
	private val albumDao: AlbumDao
) : SongsRepository {
	override fun getSongsStream(): Flow<List<Song>> =
		songDao.getSongEntitiesStream()
			.map { it.map(SongEntity::asExternalModel) }

	override fun getSongStream(mediaId: String): Flow<Song> =
		songDao.getSongEntityStream(mediaId)
			.map { it.asExternalModel() }

	override fun getSongForAlbum(albumId: Long): Flow<List<Song>> =
		songDao.getSongEntitiesStream()
			.map { it.map(SongEntity::asExternalModel).filter { song -> song.albumId == albumId } }
}