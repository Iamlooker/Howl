package com.looker.howlmusic.utils

import android.content.Context
import com.looker.data_music.data.AlbumsRepository
import com.looker.howlmusic.service.MusicServiceConnection
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

	@Singleton
	@Provides
	fun provideMusicServiceConnection(
		@ApplicationContext context: Context
	): MusicServiceConnection = MusicServiceConnection(context)

	@Singleton
	@Provides
	fun provideAlbumsRepository(
		@ApplicationContext context: Context
	): AlbumsRepository = AlbumsRepository(context)
}