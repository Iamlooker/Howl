package com.looker.core_database

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
	@Provides
	@Singleton
	fun provideHowlDatabase(
		@ApplicationContext context: Context
	) : HowlDatabase = Room.databaseBuilder(
		context,
		HowlDatabase::class.java,
		"howl-database"
	).build()
}