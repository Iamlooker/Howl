package com.looker.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.looker.core.database.dao.AlbumDao
import com.looker.core.database.dao.BlacklistDao
import com.looker.core.database.dao.SongDao
import com.looker.core.database.model.AlbumEntity
import com.looker.core.database.model.BlacklistEntity
import com.looker.core.database.model.SongEntity
import com.looker.core.database.utils.InstantConverters

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