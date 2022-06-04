package com.looker.core_database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.looker.core_database.dao.AlbumDao
import com.looker.core_database.dao.SongDao
import com.looker.core_database.model.AlbumEntity
import com.looker.core_database.model.SongEntity

@Database(
	entities = [
		SongEntity::class,
		AlbumEntity::class
	],
	version = 1,

)
abstract class HowlDatabase : RoomDatabase() {
	abstract fun songDao() : SongDao
	abstract fun albumDao() : AlbumDao
}