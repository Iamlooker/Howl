package com.looker.core_data.repository

import com.looker.core_model.Album
import kotlinx.coroutines.flow.Flow

interface AlbumsRepository {
	fun getAlbumsStream(): Flow<List<Album>>
	fun getAlbumStream(albumId: Long): Flow<Album>
}