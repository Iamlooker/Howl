package com.looker.howlmusic.utils.extension

import android.support.v4.media.MediaMetadataCompat
import com.looker.domain_music.Song
import com.looker.domain_music.emptySong

inline val MediaMetadataCompat?.toSong
	get() = this?.description?.let {
		Song(
			mediaId = it.mediaId ?: "",
			songUri = it.mediaUri.toString(),
			name = it.title.toString(),
			artistName = it.subtitle.toString(),
			albumName = null,
			albumArt = it.iconUri.toString()
		)
	} ?: emptySong

inline val Song.toMediaMetadataCompat: MediaMetadataCompat
	get() = this.let { song ->
		MediaMetadataCompat.Builder()
			.putString(MediaMetadataCompat.METADATA_KEY_ARTIST, song.artistName)
			.putString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID, song.mediaId)
			.putString(MediaMetadataCompat.METADATA_KEY_TITLE, song.name)
			.putString(MediaMetadataCompat.METADATA_KEY_DISPLAY_TITLE, song.name)
			.putString(MediaMetadataCompat.METADATA_KEY_DISPLAY_ICON_URI, song.albumArt)
			.putString(MediaMetadataCompat.METADATA_KEY_MEDIA_URI, song.songUri)
			.putString(MediaMetadataCompat.METADATA_KEY_ALBUM_ART_URI, song.albumArt)
			.putString(MediaMetadataCompat.METADATA_KEY_DISPLAY_SUBTITLE, song.artistName)
			.putString(MediaMetadataCompat.METADATA_KEY_DISPLAY_DESCRIPTION, song.artistName)
			.putString(MediaMetadataCompat.METADATA_KEY_ALBUM, song.albumName)
			.build()
	}

inline val MediaMetadataCompat.id: String?
	get() = getString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID)