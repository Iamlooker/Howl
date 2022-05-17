package com.looker.howlmusic.utils.extension

import android.support.v4.media.MediaMetadataCompat
import com.looker.core_model.Song

inline val MediaMetadataCompat?.toSong
	get() = this?.description?.let {
		Song(
			mediaId = it.mediaId ?: "",
			pathUri = it.mediaUri.toString(),
			name = it.title.toString(),
			artist = it.subtitle.toString(),
			albumArt = it.iconUri.toString()
		)
	} ?: Song()

inline val Song.toMediaMetadataCompat: MediaMetadataCompat
	get() = this.let { song ->
		MediaMetadataCompat.Builder()
			.putString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID, song.mediaId)
			.putString(MediaMetadataCompat.METADATA_KEY_DISPLAY_ICON_URI, song.albumArt)
			.putString(MediaMetadataCompat.METADATA_KEY_ALBUM_ART_URI, song.albumArt)
			.putString(MediaMetadataCompat.METADATA_KEY_TITLE, song.name)
			.putString(MediaMetadataCompat.METADATA_KEY_DISPLAY_TITLE, song.name)
			.putString(MediaMetadataCompat.METADATA_KEY_MEDIA_URI, song.pathUri)
			.putString(MediaMetadataCompat.METADATA_KEY_ARTIST, song.artist)
			.putString(MediaMetadataCompat.METADATA_KEY_DISPLAY_SUBTITLE, song.artist)
			.putString(MediaMetadataCompat.METADATA_KEY_DISPLAY_DESCRIPTION, song.artist)
			.putString(MediaMetadataCompat.METADATA_KEY_ALBUM, song.album)
			.build()
	}

inline val MediaMetadataCompat.id: String?
	get() = getString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID)