package com.looker.core_database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.looker.core_database.dao.AlbumDao
import com.looker.core_database.dao.BlacklistDao
import com.looker.core_database.dao.SongDao
import com.looker.core_database.model.AlbumEntity
import com.looker.core_database.model.BlacklistEntity
import com.looker.core_database.model.SongEntity
import com.looker.core_database.utils.InstantConverters

@Database(
	entities = [
		SongEntity::class,
		AlbumEntity::class,
		BlacklistEntity::class
	],
	version = 6
)
@TypeConverters(InstantConverters::class)
abstract class HowlDatabase : RoomDatabase() {
	abstract fun songDao(): SongDao
	abstract fun albumDao(): AlbumDao
	abstract fun blacklistDao(): BlacklistDao
}