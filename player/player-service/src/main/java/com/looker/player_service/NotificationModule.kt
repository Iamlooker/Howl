package com.looker.player_service

import android.app.NotificationManager
import android.content.Context
import android.media.session.MediaSession
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ServiceScoped

@InstallIn(ServiceComponent::class)
@Module
object NotificationModule {

    @ServiceScoped
    @Provides
    fun provideMediaSession(@ApplicationContext context: Context): MediaSession =
        MediaSession(context, "howlmusic")

    @ServiceScoped
    @Provides
    fun provideNotificationManger(@ApplicationContext context: Context): NotificationManager =
        context.getSystemService(NotificationManager::class.java) as NotificationManager
}