package com.looker.core.service.extensions

import android.os.Bundle
import android.support.v4.media.MediaMetadataCompat
import androidx.core.net.toUri
import com.google.android.exoplayer2.MediaMetadata
import com.google.android.exoplayer2.util.MimeTypes
import com.looker.core.model.Song

inline val MediaMetadataCompat?.toSong
	get() = this?.let { metadata ->
		Song(
			mediaId = metadata.id.toString(),
			pathUri = metadata.mediaUri.toString(),
			name = metadata.title.toString(),
			album = metadata.album.toString(),
			artist = metadata.artist.toString(),
			albumArt = metadata.albumArtUri.toString(),
			duration = metadata.duration
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
			.putLong(MediaMetadataCompat.METADATA_KEY_DURATION, song.duration)
			.build()
	}

fun MediaMetadataCompat.toMediaItemMetadata(): MediaMetadata {
	return with(MediaMetadata.Builder()) {
		setTitle(title)
		setAlbumArtist(artist)
		setAlbumTitle(album)
		setArtworkUri(albumArtUri?.toUri())
		val extras = Bundle()
		extras.putLong(MediaMetadataCompat.METADATA_KEY_DURATION, duration)
		setExtras(extras)
	}.build()
}

fun MediaMetadataCompat.toMediaItem(): com.google.android.exoplayer2.MediaItem {
	return with(com.google.android.exoplayer2.MediaItem.Builder()) {
		setMediaId(mediaUri.toString())
		setUri(mediaUri)
		setMimeType(MimeTypes.AUDIO_MPEG)
		setMediaMetadata(toMediaItemMetadata())
	}.build()
}

inline val MediaMetadataCompat.id: String?
	get() = getString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID)

inline val MediaMetadataCompat.title: String?
	get() = getString(MediaMetadataCompat.METADATA_KEY_TITLE)

inline val MediaMetadataCompat.artist: String?
	get() = getString(MediaMetadataCompat.METADATA_KEY_DISPLAY_DESCRIPTION)

inline val MediaMetadataCompat.album: String?
	get() = getString(MediaMetadataCompat.METADATA_KEY_ALBUM)

inline val MediaMetadataCompat.albumArtUri: String?
	get() = getString(MediaMetadataCompat.METADATA_KEY_DISPLAY_ICON_URI)

inline val MediaMetadataCompat.mediaUri: String?
	get() = getString(MediaMetadataCompat.METADATA_KEY_MEDIA_URI)

inline val MediaMetadataCompat.duration
	get() = getLong(MediaMetadataCompat.METADATA_KEY_DURATION)