package com.looker.howlmusic.service

import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaBrowserCompat.MediaItem.FLAG_PLAYABLE
import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.MediaMetadataCompat.*
import androidx.core.net.toUri
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.source.ConcatenatingMediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSource
import com.looker.data_music.data.SongsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MusicSource @Inject constructor(private val songsRepository: SongsRepository) {

	var songs = emptyList<MediaMetadataCompat>()

	suspend fun fetchMediaData() = withContext(Dispatchers.IO) {
		state = State.STATE_INITIALIZING
		songs = songsRepository.getAllSongs().map { song ->
			Builder()
				.putString(METADATA_KEY_ARTIST, song.artistName)
				.putString(METADATA_KEY_MEDIA_ID, song.mediaId)
				.putString(METADATA_KEY_TITLE, song.songName)
				.putString(METADATA_KEY_DISPLAY_TITLE, song.songName)
				.putString(METADATA_KEY_DISPLAY_ICON_URI, song.albumArt)
				.putString(METADATA_KEY_MEDIA_URI, song.songUri)
				.putString(METADATA_KEY_ALBUM_ART_URI, song.albumArt)
				.putString(METADATA_KEY_DISPLAY_SUBTITLE, song.artistName)
				.putString(METADATA_KEY_DISPLAY_DESCRIPTION, song.artistName)
				.build()
		}
		launch(Dispatchers.Main) { state = State.STATE_INITIALIZED }
	}

	fun asMediaSource(dataSourceFactory: DefaultDataSource.Factory): ConcatenatingMediaSource {
		val concatenatingMediaSource = ConcatenatingMediaSource()
		songs.forEach { song ->
			val mediaItem = MediaItem.fromUri(song.getString(METADATA_KEY_MEDIA_URI))
			val mediaSource = ProgressiveMediaSource.Factory(dataSourceFactory)
				.createMediaSource(mediaItem)
			concatenatingMediaSource.addMediaSource(mediaSource)
		}
		return concatenatingMediaSource
	}

	fun asMediaItem() = songs.map { song ->
		val description = MediaDescriptionCompat.Builder()
			.setMediaUri(song.getString(METADATA_KEY_MEDIA_URI).toUri())
			.setTitle(song.description.title)
			.setSubtitle(song.description.subtitle)
			.setMediaId(song.description.mediaId)
			.setIconUri(song.description.iconUri)
			.build()
		MediaBrowserCompat.MediaItem(description, FLAG_PLAYABLE)
	}.toMutableList()

	private val onReadyListener = mutableListOf<(Boolean) -> Unit>()

	private var state: State = State.STATE_CREATED
		set(value) {
			if (value == State.STATE_INITIALIZED || value == State.STATE_ERROR) {
				synchronized(onReadyListener) {
					field = value
					onReadyListener.forEach { listener ->
						listener(state == State.STATE_INITIALIZED)
					}
				}
			} else {
				field = value
			}
		}

	fun whenReady(action: (Boolean) -> Unit): Boolean {
		return if (state == State.STATE_CREATED || state == State.STATE_INITIALIZING) {
			onReadyListener += action
			false
		} else {
			action(state == State.STATE_INITIALIZED)
			true
		}
	}
}

enum class State {
	STATE_CREATED,
	STATE_INITIALIZING,
	STATE_INITIALIZED,
	STATE_ERROR
}