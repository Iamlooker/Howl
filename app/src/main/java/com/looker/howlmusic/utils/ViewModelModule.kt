package com.looker.howlmusic.utils

import android.content.Context
import com.looker.data_music.data.AlbumsRepository
import com.looker.data_music.data.SongsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object ViewModelModule {

    @Singleton
    @Provides
    fun provideSongsRepository(
        @ApplicationContext context: Context,
    ): SongsRepository = SongsRepository(context)

    @Singleton
    @Provides
    fun provideAlbumsRepository(
        @ApplicationContext context: Context,
    ): AlbumsRepository = AlbumsRepository(context)

    @Singleton
    @Provides
    fun provideIODispatcher(): CoroutineDispatcher = Dispatchers.IO
}