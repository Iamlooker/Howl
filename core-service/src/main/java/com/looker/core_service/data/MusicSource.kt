package com.looker.core_service.data

import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaBrowserCompat.MediaItem.FLAG_PLAYABLE
import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.MediaMetadataCompat.METADATA_KEY_MEDIA_URI
import androidx.core.net.toUri
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.source.ConcatenatingMediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSource
import com.looker.core_data.repository.BlacklistsRepository
import com.looker.core_data.repository.SongsRepository
import com.looker.core_service.utils.extension.toMediaMetadataCompat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject

class MusicSource @Inject constructor(
	songsRepository: SongsRepository,
	blacklistsRepository: BlacklistsRepository
) : DataSource<MediaMetadataCompat> {

	override var data = emptyList<MediaMetadataCompat>()

	@State
	private var state: Int = STATE_CREATED
		set(value) {
			if (value == STATE_INITIALIZED || value == STATE_ERROR) {
				synchronized(onReadyListeners) {
					field = value
					onReadyListeners.forEach { listener ->
						listener(state == STATE_INITIALIZED)
					}
				}
			} else {
				field = value
			}
		}

	init {
		state = STATE_INITIALIZING
	}

	override suspend fun load() {
		fetchMediaData()?.let { songList ->
			data = songList
			state = STATE_INITIALIZED
		} ?: run {
			data = emptyList()
			state = STATE_ERROR
		}
	}

	private val songsState = combine(
		songsRepository.getSongsStream(),
		blacklistsRepository.getBlacklistSongs()
	) { songsResult, blacklist ->
		val blacklistedSongs = blacklist.flatMap { it.songsFromAlbum }
		songsResult.filterNot { it.albumId.toString() in blacklistedSongs }
			.map { it.toMediaMetadataCompat }
	}

	private suspend fun fetchMediaData() = withContext(Dispatchers.IO) {
		try {
			songsState.first()
		} catch (ioException: IOException) {
			null
		}
	}

	override fun asMediaSource(dataSourceFactory: DefaultDataSource.Factory): ConcatenatingMediaSource {
		val concatenatingMediaSource = ConcatenatingMediaSource()
		data.forEach { song ->
			val mediaItem = MediaItem.fromUri(song.getString(METADATA_KEY_MEDIA_URI) ?: "null")
			val mediaSource = ProgressiveMediaSource.Factory(dataSourceFactory)
				.createMediaSource(mediaItem)
			concatenatingMediaSource.addMediaSource(mediaSource)
		}
		return concatenatingMediaSource
	}

	override fun asMediaItem() = data.map { song ->
		val mediaUri = song.getString(METADATA_KEY_MEDIA_URI) ?: "null"
		val description = MediaDescriptionCompat.Builder()
			.setMediaUri(mediaUri.toUri())
			.setTitle(song.description.title)
			.setSubtitle(song.description.subtitle)
			.setMediaId(song.description.mediaId ?: "null")
			.setIconUri(song.description.iconUri)
			.build()
		MediaBrowserCompat.MediaItem(description, FLAG_PLAYABLE)
	}.toMutableList()

	private val onReadyListeners = mutableListOf<(Boolean) -> Unit>()

	override fun sourceReady(action: (Boolean) -> Unit): Boolean =
		when (state) {
			STATE_CREATED, STATE_INITIALIZING -> {
				onReadyListeners += action
				false
			}
			else -> {
				action(state != STATE_ERROR)
				true
			}
		}
}