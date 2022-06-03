package com.looker.feature_player.service

import android.support.v4.media.MediaBrowserCompat
import com.google.android.exoplayer2.source.ConcatenatingMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSource

interface DataSource<T> {

	var data: List<T>

	suspend fun load()

	fun asMediaSource(dataSourceFactory: DefaultDataSource.Factory): ConcatenatingMediaSource

	fun asMediaItem(): MutableList<MediaBrowserCompat.MediaItem>

	fun sourceReady(action: (Boolean) -> Unit): Boolean
}

enum class State {
	STATE_CREATED,
	STATE_INITIALIZING,
	STATE_INITIALIZED,
	STATE_ERROR
}