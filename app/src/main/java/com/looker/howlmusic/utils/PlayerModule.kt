package com.looker.howlmusic.utils

import android.content.Context
import com.google.android.exoplayer2.SimpleExoPlayer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
object PlayerModule {

    @Provides
    fun providePlayer(
        @ApplicationContext context: Context
    ): SimpleExoPlayer = SimpleExoPlayer.Builder(context).build()
}