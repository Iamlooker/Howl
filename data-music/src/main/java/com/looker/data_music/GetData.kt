package com.looker.data_music

import android.content.Context
import androidx.startup.AppInitializer
import androidx.startup.Initializer
import androidx.work.ExistingWorkPolicy
import androidx.work.WorkManager
import androidx.work.WorkManagerInitializer

object GetData {
	fun initialize(context: Context) {
		AppInitializer.getInstance(context).initializeComponent(GetDataInitializer::class.java)
	}
}

const val GetDataWork = "GetDataWork"

internal class GetDataInitializer : Initializer<GetData> {
	override fun create(context: Context): GetData {
		WorkManager.getInstance(context).apply {
			enqueueUniqueWork(
				GetDataWork,
				ExistingWorkPolicy.KEEP,
				SyncWorker.syncDataRequest
			)
		}
		return GetData
	}

	override fun dependencies(): List<Class<WorkManagerInitializer>> =
		listOf(WorkManagerInitializer::class.java)

}