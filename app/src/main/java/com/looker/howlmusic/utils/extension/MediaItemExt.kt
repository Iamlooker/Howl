package com.looker.howlmusic.utils.extension

import android.support.v4.media.MediaBrowserCompat
import com.looker.domain_music.Song

inline val MediaBrowserCompat.MediaItem.toSong
	get() = Song(
		mediaId!!,
		description.mediaUri.toString(),
		0,
		null,
		description.title.toString(),
		description.subtitle.toString(),
		null,
		description.iconUri.toString()
	)