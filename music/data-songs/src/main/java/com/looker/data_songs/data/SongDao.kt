package com.looker.data_songs.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface SongDao {
    @Query("SELECT * FROM song")
    fun loadAllSongs(): List<Song>

    @Query("SELECT * FROM song WHERE albumId IN (:albumIds)")
    fun loadByAlbumId(vararg albumIds: Int): List<Song>

    @Insert
    fun insert(vararg songs: Song)

    @Delete
    fun delete(song: Song)
}