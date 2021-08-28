package com.looker.data_songs.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters


@Database(
    entities = [Song::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class SongsDatabase : RoomDatabase() {

    abstract fun songDao(): SongDao

    companion object {

        @Volatile
        private var instance: SongsDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: createDatabase(context).also { instance = it }
        }

        private fun createDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                SongsDatabase::class.java,
                "songs-database"
            ).build()
    }
}