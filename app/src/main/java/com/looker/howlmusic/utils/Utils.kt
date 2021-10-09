package com.looker.howlmusic.utils

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import com.looker.constants.Constants.READ_PERMISSION
import com.looker.data_music.data.AlbumsRepository
import com.looker.data_music.data.SongsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

fun checkReadPermission(context: Context) =
    ContextCompat.checkSelfPermission(
        context, READ_PERMISSION
    ) == PackageManager.PERMISSION_GRANTED

@InstallIn(SingletonComponent::class)
@Module
object Utils {

    @Singleton
    @Provides
    fun provideSongsRepository(
        @ApplicationContext context: Context
    ): SongsRepository = SongsRepository(context)

    @Singleton
    @Provides
    fun provideAlbumsRepository(
        @ApplicationContext context: Context
    ): AlbumsRepository = AlbumsRepository(context)
}