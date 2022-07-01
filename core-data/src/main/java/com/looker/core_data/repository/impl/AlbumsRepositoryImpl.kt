package com.looker.core_data.repository.impl

import android.content.Context
import com.looker.core_data.repository.AlbumsRepository
import com.looker.core_data.repository.SongsRepository
import com.looker.core_database.dao.AlbumDao
import com.looker.core_database.model.AlbumEntity
import com.looker.core_database.model.asEntity
import com.looker.core_database.model.asExternalModel
import com.looker.core_model.Album
import com.looker.core_model.Song
import com.looker.music_data.AlbumsData
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AlbumsRepositoryImpl @Inject constructor(
	@ApplicationContext
	private val appContext: Context,
	private val songsRepository: SongsRepository,
	private val albumDao: AlbumDao
) : AlbumsRepository {
	override fun getAlbumsStream(): Flow<List<Album>> =
		albumDao.getAlbumEntitiesStream()
			.map { it.map(AlbumEntity::asExternalModel) }

	override fun getAlbumStream(albumId: Long): Flow<Album> =
		albumDao.getAlbumEntityStream(albumId).map(AlbumEntity::asExternalModel)

	override fun getRelatedSongs(albumId: Long): Flow<List<Song>> =
		songsRepository.getSongForAlbum(albumId)

	override suspend fun syncData(): Boolean {
		val albums = AlbumsData(appContext).createAlbumsList().map { it.asEntity() }
		getAlbumsStream().first { albumsList ->
			val removedAlbums = albumsList.filter { it.asEntity() !in albums }
				.map(Album::albumId)
			albumDao.deleteAlbums(removedAlbums)
			true
		}
		albumDao.insertOrIgnoreAlbums(albums)
		return albums.isNotEmpty()
	}

	override fun getAllSongs(): Flow<List<Song>> =
		songsRepository.getSongsStream()
}