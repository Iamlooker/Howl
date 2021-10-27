package com.looker.data_music.db

import androidx.room.*
import com.looker.domain_music.Song

@Dao
abstract class SongsDao {

    @Query("SELECT COUNT(*) FROM songs")
    abstract suspend fun count(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertSong(song: Song): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertAllSongs(songs: List<Song>)

    @Update
    abstract suspend fun updateSong(song: Song)

    @Delete
    abstract suspend fun deleteSong(song: Song): Int
}
