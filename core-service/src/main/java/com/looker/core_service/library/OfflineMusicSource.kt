package com.looker.core_service.library

import android.support.v4.media.MediaMetadataCompat
import com.looker.core_common.OrderType
import com.looker.core_common.order.SongOrder
import com.looker.core_data.repository.BlacklistsRepository
import com.looker.core_data.repository.SongsRepository
import com.looker.core_data.use_case.sortBy
import com.looker.core_model.Song
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
	): List<MediaMetadataCompat> = withContext(Dispatchers.IO) {
		val blacklistSong =
			blacklistsRepository.getBlacklistSongs().first().flatMap { it.songsFromAlbum }
		val allSongs = songsRepository.getSongsStream().first()
			.filterNot { it.albumId.toString() in blacklistSong }
			.sortBy(SongOrder.Title(OrderType.Ascending))
			.map(Song::toMediaMetadataCompat)
		allSongs.forEach { it.description.extras?.putAll(it.bundle) }
		allSongs
	}
}