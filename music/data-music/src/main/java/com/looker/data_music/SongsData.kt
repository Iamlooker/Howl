package com.looker.data_music

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import com.looker.data_music.SongsConstants.externalUri
import com.looker.data_music.SongsConstants.isMusic
import com.looker.data_music.SongsConstants.songsProjections
import com.looker.data_music.SongsConstants.sortOrderSong
import com.looker.domain_music.Song
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow

private object SongsConstants {
    val songsProjections = arrayOf(
        MediaStore.Audio.Media._ID,
        MediaStore.Audio.Media.ALBUM_ID,
        MediaStore.Audio.Genres._ID,
        MediaStore.Audio.Media.TITLE,
        MediaStore.Audio.Media.ARTIST,
        MediaStore.Audio.Media.ALBUM,
        MediaStore.Audio.Media.DURATION
    )

    val externalUri: Uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI

    const val isMusic = MediaStore.Audio.Media.IS_MUSIC + " != 0"
    const val sortOrderSong = MediaStore.Audio.Media.TITLE + " COLLATE NOCASE ASC"
}

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
                val genreId = songCursor.getLong(2)
                val songName = songCursor.getString(3)
                val artistName = songCursor.getString(4)
                val albumName = songCursor.getString(5)
                val songDurationMillis = songCursor.getLong(6)
                val songUri = "$externalUri/$songId"
                emit(
                    Song(
                        songUri = songUri,
                        albumId = albumId,
                        genreId = genreId,
                        songName = songName,
                        artistName = artistName,
                        albumName = albumName,
                        duration = songDurationMillis
                    )
                )
            } while (songCursor.moveToNext())
        }
        songCursor?.close()
    }
}