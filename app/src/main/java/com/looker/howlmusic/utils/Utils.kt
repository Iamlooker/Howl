package com.looker.howlmusic.utils

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import coil.ImageLoader
import com.looker.constants.Constants.READ_PERMISSION
import com.looker.howlmusic.R
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
    fun provideImageLoader(
        @ApplicationContext context: Context
    ): ImageLoader = ImageLoader.Builder(context)
        .placeholder(R.drawable.white_background)
        .error(R.drawable.error_image)
        .build()
}