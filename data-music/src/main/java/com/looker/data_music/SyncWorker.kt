package com.looker.data_music

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.*
import com.looker.core_database.dao.SongDao
import com.looker.core_database.model.asEntity
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import javax.inject.Inject

@HiltWorker
class SyncWorker @AssistedInject constructor(
	@Assisted val appContext: Context,
	@Assisted workerParams: WorkerParameters,
	val songDao: SongDao
) : CoroutineWorker(appContext, workerParams) {
	override suspend fun doWork(): Result {
		val songs = SongsData(appContext).createSongsList().map { it.asEntity() }
		songDao.insertOrIgnoreSongs(songs)
		return Result.success()
	}

	companion object {
		val syncDataRequest = OneTimeWorkRequestBuilder<DelegatingWorker>()
			.setConstraints(SyncConstraints)
			.setInputData(SyncWorker::class.delegatedData())
			.build()
	}
}

private val SyncConstraints = Constraints.Builder()
	.setRequiredNetworkType(NetworkType.NOT_REQUIRED)
	.build()