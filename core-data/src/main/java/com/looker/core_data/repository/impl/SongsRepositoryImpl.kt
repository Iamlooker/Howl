package com.looker.core_data.repository.impl

import android.content.Context
import com.looker.core_data.repository.SongsRepository
import com.looker.core_database.dao.AlbumDao
import com.looker.core_database.dao.SongDao
import com.looker.core_database.model.SongEntity
import com.looker.core_database.model.asEntity
import com.looker.core_database.model.asExternalModel
import com.looker.core_model.Song
import com.looker.data_music.SongsData
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SongsRepositoryImpl @Inject constructor(
	@ApplicationContext
	private val appContext: Context,
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

	override suspend fun syncData(): Boolean {
		val songs = SongsData(appContext).createSongsList().map { it.asEntity() }
		songDao.insertOrIgnoreSongs(songs)
		return true
	}
}