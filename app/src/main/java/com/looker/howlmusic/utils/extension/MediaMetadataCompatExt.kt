package com.looker.howlmusic.utils.extension

import android.support.v4.media.MediaMetadataCompat
import com.looker.domain_music.Song

inline val MediaMetadataCompat?.toSong
	get() = Song(
		this?.description?.mediaId.toString(),
		this?.description?.mediaUri.toString(),
		0,
		null,
		this?.description?.title.toString(),
		this?.description?.subtitle.toString(),
		null,
		this?.description?.mediaUri.toString()
	)