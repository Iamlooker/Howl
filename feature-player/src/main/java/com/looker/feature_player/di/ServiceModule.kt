package com.looker.feature_player.di

import android.content.Context
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.DefaultRenderersFactory
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.RenderersFactory
import com.google.android.exoplayer2.audio.AudioAttributes
import com.google.android.exoplayer2.upstream.DefaultDataSource
import com.looker.data_music.data.SongsRepository
import com.looker.feature_player.service.MusicSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ServiceScoped

@Module
@InstallIn(ServiceComponent::class)
object ServiceModule {

	@ServiceScoped
	@Provides
	fun provideDataSourceFactory(
		@ApplicationContext context: Context
	): DefaultDataSource.Factory = DefaultDataSource.Factory(context)

	@ServiceScoped
	@Provides
	fun provideRenderersFactory(
		@ApplicationContext context: Context
	): RenderersFactory = DefaultRenderersFactory(context)

	@ServiceScoped
	@Provides
	fun provideAudioAttributes(): AudioAttributes = AudioAttributes.Builder()
		.setUsage(C.USAGE_MEDIA)
		.setContentType(C.CONTENT_TYPE_MUSIC)
		.build()

	@ServiceScoped
	@Provides
	fun provideExoPlayer(
		@ApplicationContext context: Context,
		renderersFactory: RenderersFactory,
		audioAttributes: AudioAttributes
	): ExoPlayer = ExoPlayer.Builder(context, renderersFactory)
		.setAudioAttributes(audioAttributes, true)
		.build()

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