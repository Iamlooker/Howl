package com.looker.core.service.di

import android.content.Context
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.DefaultRenderersFactory
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.RenderersFactory
import com.google.android.exoplayer2.audio.AudioAttributes
import com.google.android.exoplayer2.upstream.DefaultDataSource
import com.looker.core.data.repository.BlacklistsRepository
import com.looker.core.data.repository.SongsRepository
import com.looker.core.service.PersistentStorage
import com.looker.core.service.library.MusicSource
import com.looker.core.service.library.OfflineMusicSource
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
		.setContentType(C.AUDIO_CONTENT_TYPE_MUSIC)
		.build()

	@ServiceScoped
	@Provides
	fun provideExoPlayer(
		@ApplicationContext context: Context,
		renderersFactory: RenderersFactory,
		audioAttributes: AudioAttributes
	): ExoPlayer = ExoPlayer.Builder(context, renderersFactory)
		.setAudioAttributes(audioAttributes, true)
		.setUsePlatformDiagnostics(false)
		.setHandleAudioBecomingNoisy(true)
		.build()

	@ServiceScoped
	@Provides
	fun provideMusicSource(
		songsRepository: SongsRepository,
		blacklistsRepository: BlacklistsRepository
	): MusicSource =
		OfflineMusicSource(songsRepository, blacklistsRepository)

	@ServiceScoped
	@Provides
	fun providePersistentStorage(
		@ApplicationContext context: Context
	): PersistentStorage = PersistentStorage.getInstance(context)
}