package com.looker.core.data.repository

import com.looker.core.model.Album
import com.looker.core.model.Song
import kotlinx.coroutines.flow.Flow

interface AlbumsRepository {
	fun getAlbumsStream(): Flow<List<Album>>
	fun getAlbumStream(albumId: Long): Flow<Album>
	fun getRelatedSongs(albumId: Long): Flow<List<Song>>
	suspend fun syncData(): Boolean

	//Temp
	fun getAllSongs(): Flow<List<Song>>
}