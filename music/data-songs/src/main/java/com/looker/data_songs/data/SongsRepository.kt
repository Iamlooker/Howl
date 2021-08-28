package com.looker.data_songs.data

import android.content.Context
import com.looker.data_songs.SongsData

class SongsRepository(private val db: SongsDatabase) {
    suspend fun getAllSongs(context: Context) =
        SongsData(context).getSongsList()

    fun insertSong(song: Song) = db.songDao().insert(song)

    suspend fun insertAllSongs(context: Context) = getAllSongs(context)

    fun getSavedSongs() = db.songDao().loadAllSongs()

    fun deleteSong(song: Song) = db.songDao().delete(song)
}