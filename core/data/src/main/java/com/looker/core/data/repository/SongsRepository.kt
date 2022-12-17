package com.looker.core.data.repository

import com.looker.core.model.Song
import kotlinx.coroutines.flow.Flow

interface SongsRepository {
	fun getSongsStream(): Flow<List<Song>>
	fun getSongStream(mediaId: String): Flow<Song>
	fun getSongForAlbum(albumId: Long): Flow<List<Song>>
	suspend fun syncData(): Boolean
	suspend fun cleanup(): Boolean
}