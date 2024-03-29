package com.looker.core.database

import com.looker.core.database.dao.AlbumDao
import com.looker.core.database.dao.BlacklistDao
import com.looker.core.database.dao.SongDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DaoModule {
	@Provides
	fun provideSongDao(
		database: HowlDatabase
	): SongDao = database.songDao()

	@Provides
	fun provideAlbumDao(
		database: HowlDatabase
	): AlbumDao = database.albumDao()

	@Provides
	fun provideBlacklistDao(
		database: HowlDatabase
	): BlacklistDao = database.blacklistDao()
}