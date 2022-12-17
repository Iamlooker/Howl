package com.looker.core.service.extensions

import android.support.v4.media.MediaBrowserCompat
import com.looker.core.model.Song

inline val MediaBrowserCompat.MediaItem.toSong
	get() = Song(
		mediaId = mediaId!!,
		name = description.title.toString(),
		artist = description.subtitle.toString(),
		albumArt = description.iconUri.toString(),
		pathUri = description.mediaUri.toString()
	)