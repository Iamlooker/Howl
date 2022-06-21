package com.looker.data_music

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.*
import com.looker.core_data.repository.AlbumsRepository
import com.looker.core_data.repository.SongsRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
internal class SyncWorker @AssistedInject constructor(
	@Assisted val appContext: Context,
	@Assisted workerParams: WorkerParameters,
	private val songsRepository: SongsRepository,
	private val albumsRepository: AlbumsRepository
) : CoroutineWorker(appContext, workerParams) {
	override suspend fun doWork(): Result {
		songsRepository.syncData()
		albumsRepository.syncData()
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