package com.looker.feature_player.di

import android.content.Context
import com.looker.feature_player.service.MusicServiceConnection
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