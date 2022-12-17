package com.looker.core.data

import com.looker.core.data.repository.AlbumsRepository
import com.looker.core.data.repository.BlacklistsRepository
import com.looker.core.data.repository.SongsRepository
import com.looker.core.data.repository.impl.AlbumsRepositoryImpl
import com.looker.core.data.repository.impl.BlacklistsRepositoryImpl
import com.looker.core.data.repository.impl.SongsRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

	@Binds
	fun bindsAlbumsRepository(
		albumsRepository: AlbumsRepositoryImpl
	): AlbumsRepository

	@Binds
	fun bindsSongsRepository(
		songsRepository: SongsRepositoryImpl
	): SongsRepository

	@Binds
	fun bindsBlacklistsRepository(
		blacklistsRepository: BlacklistsRepositoryImpl
	): BlacklistsRepository
}