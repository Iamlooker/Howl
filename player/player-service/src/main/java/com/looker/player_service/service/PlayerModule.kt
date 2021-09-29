package com.looker.player_service.service

import android.content.Context
import com.google.android.exoplayer2.SimpleExoPlayer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object PlayerModule {

    @Singleton
    @Provides
    fun providePlayer(
        @ApplicationContext context: Context
    ): SimpleExoPlayer = SimpleExoPlayer.Builder(context).build()
}