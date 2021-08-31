package com.looker.data_music

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import com.looker.data_music.AlbumsConstants.albumsProjections
import com.looker.data_music.AlbumsConstants.externalUri
import com.looker.data_music.AlbumsConstants.isMusic
import com.looker.data_music.AlbumsConstants.sortOrderAlbum
import com.looker.data_music.data.Album
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow

private object AlbumsConstants {
    val albumsProjections = arrayOf(
        MediaStore.Audio.Media.ALBUM_ID,
        MediaStore.Audio.Media.ALBUM,
        MediaStore.Audio.Media.ARTIST,
    )

    val externalUri: Uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI

    const val isMusic = MediaStore.Audio.Media.IS_MUSIC + " != 0"
    const val sortOrderAlbum = MediaStore.Audio.Media.ALBUM + " COLLATE NOCASE ASC"
}

class AlbumsData(context: Context) {

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