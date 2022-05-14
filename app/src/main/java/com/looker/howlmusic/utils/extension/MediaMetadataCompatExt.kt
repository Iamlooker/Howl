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

inline val Song.toMediaMetadataCompat: MediaMetadataCompat
	get() = this.let { song ->
		MediaMetadataCompat.Builder()
			.putString(MediaMetadataCompat.METADATA_KEY_ARTIST, song.artistName)
			.putString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID, song.mediaId)
			.putString(MediaMetadataCompat.METADATA_KEY_TITLE, song.songName)
			.putString(MediaMetadataCompat.METADATA_KEY_DISPLAY_TITLE, song.songName)
			.putString(MediaMetadataCompat.METADATA_KEY_DISPLAY_ICON_URI, song.albumArt)
			.putString(MediaMetadataCompat.METADATA_KEY_MEDIA_URI, song.songUri)
			.putString(MediaMetadataCompat.METADATA_KEY_ALBUM_ART_URI, song.albumArt)
			.putString(MediaMetadataCompat.METADATA_KEY_DISPLAY_SUBTITLE, song.artistName)
			.putString(MediaMetadataCompat.METADATA_KEY_DISPLAY_DESCRIPTION, song.artistName)
			.build()
	}

inline val MediaMetadataCompat.id: String?
	get() = getString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID)