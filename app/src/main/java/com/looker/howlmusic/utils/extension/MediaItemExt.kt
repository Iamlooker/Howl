package com.looker.howlmusic.utils.extension

import android.support.v4.media.MediaBrowserCompat
import com.looker.core_model.Song

inline val MediaBrowserCompat.MediaItem.toSong
	get() = Song(
		mediaId = mediaId!!,
		name = description.title.toString(),
		artist = description.subtitle.toString(),
		album = "",
		albumArt = description.iconUri.toString(),
		pathUri = description.mediaUri.toString()
	)