package com.looker.data_music.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.looker.domain_music.Song

@Database(
	entities = [Song::class],
	version = 1
)
abstract class Database : RoomDatabase() {
	abstract fun songsDao(): SongsDao
}