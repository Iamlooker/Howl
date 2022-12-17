package com.looker.core.service.extensions

import android.support.v4.media.MediaDescriptionCompat
import com.looker.core.model.Song

inline val MediaDescriptionCompat.toSong
	get() = Song(
		mediaId = mediaId.toString(),
		name = title.toString(),
		artist = subtitle.toString(),
		albumArt = iconUri.toString(),
		pathUri = mediaUri.toString()
	)