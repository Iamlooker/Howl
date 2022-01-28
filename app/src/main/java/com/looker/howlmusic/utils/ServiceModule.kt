package com.looker.howlmusic.utils

import android.content.Context
import com.google.android.exoplayer2.DefaultRenderersFactory
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.RenderersFactory
import com.google.android.exoplayer2.upstream.DefaultDataSource
import com.looker.data_music.data.SongsRepository
import com.looker.howlmusic.service.MusicSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ServiceScoped

@InstallIn(ServiceComponent::class)
@Module
object ServiceModule {

	@ServiceScoped
	@Provides
	fun provideDataSourceFactory(
		@ApplicationContext context: Context
	): DefaultDataSource.Factory =
		DefaultDataSource.Factory(context)

	@ServiceScoped
	@Provides
	fun provideRenderersFactory(
		@ApplicationContext context: Context
	): RenderersFactory = DefaultRenderersFactory(context)

	@ServiceScoped
	@Provides
	fun provideExoPlayer(
		@ApplicationContext context: Context,
		renderersFactory: RenderersFactory
	): ExoPlayer = ExoPlayer.Builder(context, renderersFactory).build()

	@ServiceScoped
	@Provides
	fun provideSongsRepository(
		@ApplicationContext context: Context
	): SongsRepository = SongsRepository(context)

	@ServiceScoped
	@Provides
	fun provideMusicSource(
		songsRepository: SongsRepository
	): MusicSource = MusicSource(songsRepository)
}