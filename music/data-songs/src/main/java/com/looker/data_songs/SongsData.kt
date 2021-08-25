package com.looker.data_songs

import android.content.Context
import com.looker.data_songs.ContentType.externalUri
import com.looker.data_songs.ContentType.isMusic
import com.looker.data_songs.ContentType.path
import com.looker.data_songs.ContentType.songsProjections
import com.looker.data_songs.ContentType.sortOrderSong
import com.looker.data_songs.data.Song
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow

class SongsData(context: Context) {

    suspend fun getSongsList(): MutableList<Song> = createSongsList()

    private suspend fun createSongsList(): MutableList<Song> {
        val list = mutableListOf<Song>()
        getSongFlow().collect {
            list.add(it)
        }
        return list
    }

    private val songCursor = context.contentResolver.query(
        externalUri,
        songsProjections,
        isMusic,
        null,
        sortOrderSong
    )

    private fun getSongFlow(): Flow<Song> = flow {

        if (songCursor?.moveToFirst() == true) {
            do {
                val songId = songCursor.getLong(0)
                val albumId = songCursor.getLong(1)
                val songName = songCursor.getString(2)
                val artistName = songCursor.getString(3)
                val albumName = songCursor.getString(4)
                val songDurationMillis = songCursor.getLong(5)
                val songUri = songId.path
                emit(
                    Song(
                        songUri,
                        albumId,
                        songName,
                        artistName,
                        albumName,
                        songDurationMillis
                    )
                )
            } while (songCursor.moveToNext())
        }
        songCursor?.close()
    }
}