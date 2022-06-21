package com.looker.core_service.data

import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import androidx.annotation.IntDef
import com.google.android.exoplayer2.source.ConcatenatingMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSource

interface DataSource<T> {
	var data: List<T>
	suspend fun load()
	fun asMediaSource(dataSourceFactory: DefaultDataSource.Factory): ConcatenatingMediaSource
	fun asMediaItem(): MutableList<MediaBrowserCompat.MediaItem>
	fun sourceReady(action: (Boolean) -> Unit): Boolean
}

@IntDef(
	STATE_CREATED,
	STATE_INITIALIZING,
	STATE_INITIALIZED,
	STATE_ERROR
)
@Retention(AnnotationRetention.SOURCE)
annotation class State

const val STATE_CREATED = 1
const val STATE_INITIALIZING = 2
const val STATE_INITIALIZED = 3
const val STATE_ERROR = 4