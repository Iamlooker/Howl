package com.looker.core_data.repository

import com.looker.core_data.Syncable
import com.looker.core_model.Song
import kotlinx.coroutines.flow.Flow

interface SongsRepository {
	fun getSongsStream(): Flow<List<Song>>
	fun getSongStream(mediaId: String): Flow<Song>
	fun getSongForAlbum(albumId: Long): Flow<List<Song>>
}