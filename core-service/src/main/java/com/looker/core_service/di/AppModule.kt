package com.looker.core_service.di

import android.content.Context
import com.looker.core_service.MusicServiceConnection
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
}