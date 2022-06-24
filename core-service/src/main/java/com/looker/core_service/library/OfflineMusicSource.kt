package com.looker.core_service.library

import android.support.v4.media.MediaMetadataCompat
import com.looker.core_data.repository.BlacklistsRepository
import com.looker.core_data.repository.SongsRepository
import com.looker.core_model.Song
import com.looker.core_service.extensions.id
import com.looker.core_service.extensions.toMediaMetadataCompat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class OfflineMusicSource @Inject constructor(
	private val songsRepository: SongsRepository,
	private val blacklistsRepository: BlacklistsRepository
) : AbstractMusicSource() {

	private var catalog: List<MediaMetadataCompat> = emptyList()

	init {
		state = STATE_INITIALIZING
	}

	override suspend fun load() {
		updateCatalog(songsRepository, blacklistsRepository).let { updatedCatalog ->
			catalog = updatedCatalog
			state = STATE_INITIALIZED
		}
	}

	override fun iterator(): Iterator<MediaMetadataCompat> = catalog.iterator()

	private suspend fun updateCatalog(
		songsRepository: SongsRepository,
		blacklistsRepository: BlacklistsRepository
	): List<MediaMetadataCompat> {
		return withContext(Dispatchers.IO) {
			val blacklistSong =
				blacklistsRepository.getBlacklistSongs().first().flatMap { it.songs }
			val musicMetadatas: List<MediaMetadataCompat> =
				songsRepository.getSongsStream().first().map(Song::toMediaMetadataCompat)
			musicMetadatas.forEach { it.description.extras?.putAll(it.bundle) }
			musicMetadatas.filterNot { it.id in blacklistSong }
		}
	}
}