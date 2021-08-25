package com.looker.data_albums

import android.content.Context
import com.looker.data_albums.ContentType.albumsProjections
import com.looker.data_albums.ContentType.externalUri
import com.looker.data_albums.ContentType.isMusic
import com.looker.data_albums.ContentType.sortOrderAlbum
import com.looker.data_albums.data.Album
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow

class AlbumsData(private val context: Context) {

    suspend fun getAlbumsList() = createAlbumsList()

    private suspend fun createAlbumsList(): List<Album> {
        val list = mutableListOf<Album>()
        getAlbumFlow().collect {
            list.add(it)
        }
        return list
    }

    private val albumCursor = context.contentResolver.query(
        externalUri,
        albumsProjections,
        isMusic,
        null,
        sortOrderAlbum
    )

    private fun getAlbumFlow(): Flow<Album> = flow {

        if (albumCursor?.moveToFirst() == true) {
            do {
                val albumId = albumCursor.getLong(0)
                val albumName = albumCursor.getString(1)
                val artistName = albumCursor.getString(2)
                emit(
                    Album(
                        albumId, albumName, artistName
                    )
                )
            } while (albumCursor.moveToNext())
        }
        albumCursor?.close()
    }
}