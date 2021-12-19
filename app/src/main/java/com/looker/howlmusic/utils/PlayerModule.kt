package com.looker.howlmusic.utils

import android.content.ComponentName
import android.content.Context
import com.google.android.exoplayer2.DefaultRenderersFactory
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.RenderersFactory
import com.looker.player_service.service.MusicService
import com.looker.player_service.service.common.MusicServiceConnection
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped

@InstallIn(ViewModelComponent::class)
@Module
object PlayerModule {

    @ViewModelScoped
    @Provides
    fun provideRenderersFactory(
        @ApplicationContext context: Context
    ): RenderersFactory = DefaultRenderersFactory(context)

    @ViewModelScoped
    @Provides
    fun providePlayer(
        @ApplicationContext context: Context,
        renderersFactory: RenderersFactory,
    ): ExoPlayer =
        ExoPlayer.Builder(context, renderersFactory).build()

    @ViewModelScoped
    @Provides
    fun provideService(
        @ApplicationContext context: Context,
    ): MusicServiceConnection = MusicServiceConnection.getInstance(
        context,
        ComponentName(context, MusicService::class.java)
    )
}