package com.looker.howlmusic.utils.extension

import android.support.v4.media.MediaMetadataCompat
import com.looker.domain_music.Song

inline val MediaMetadataCompat.toSong
	get() = description?.let {
		Song(
			it.mediaId ?: "",
			it.mediaUri.toString(),
			0,
			null,
			it.title.toString(),
			it.subtitle.toString(),
			null,
			it.iconUri.toString()
		)
	}

inline val MediaMetadataCompat.id: String?
	get() = getString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID)