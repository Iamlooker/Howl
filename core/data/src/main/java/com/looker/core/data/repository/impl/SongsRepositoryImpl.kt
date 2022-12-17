package com.looker.core.data.repository.impl

import android.content.Context
import com.looker.core.data.repository.SongsRepository
import com.looker.core.database.dao.SongDao
import com.looker.core.database.model.SongEntity
import com.looker.core.database.model.asEntity
import com.looker.core.database.model.asExternalModel
import com.looker.core.model.Song
import com.looker.music_data.SongsData
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SongsRepositoryImpl @Inject constructor(
	@ApplicationContext
	private val appContext: Context,
	private val songDao: SongDao
) : SongsRepository {
	override fun getSongsStream(): Flow<List<Song>> =
		songDao.getSongEntitiesStream()
			.map { it.map(SongEntity::asExternalModel) }

	override fun getSongStream(mediaId: String): Flow<Song> =
		songDao.getSongEntityStream(mediaId)
			.map(SongEntity::asExternalModel)

	override fun getSongForAlbum(albumId: Long): Flow<List<Song>> =
		songDao.getSongEntitiesStream()
			.map { it.map(SongEntity::asExternalModel).filter { song -> song.albumId == albumId } }

	override suspend fun syncData(): Boolean {
		val songs = SongsData(appContext).getAllSongs().map { it.asEntity() }
		songDao.insertOrIgnoreSongs(songs)
		return songs.isNotEmpty()
	}

	override suspend fun cleanup(): Boolean {
		val songs = SongsData(appContext).getAllSongs().map { it.asEntity() }
		getSongsStream().first { songsList ->
			if (songsList.isNotEmpty()) {
				val removedSongs = songsList.filter { it.asEntity() !in songs }
					.map(Song::mediaId)
				songDao.deleteSongs(removedSongs)
			}
			true
		}
		return songs.isNotEmpty()
	}
}