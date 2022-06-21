package com.looker.core_service.utils.extension

import android.support.v4.media.MediaBrowserCompat
import androidx.core.net.toUri
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.MediaMetadata
import com.looker.core_model.Song

inline val MediaBrowserCompat.MediaItem.toSong
	get() = Song(
		mediaId = mediaId!!,
		name = description.title.toString(),
		artist = description.subtitle.toString(),
		albumArt = description.iconUri.toString(),
		pathUri = description.mediaUri.toString()
	)

inline val Song.toMediaItem
	get() = MediaItem.Builder()
		.setMediaId(mediaId)
		.setMediaMetadata(
			MediaMetadata.Builder()
				.setMediaUri(pathUri.toUri())
				.setTitle(name)
				.setArtist(artist)
				.setArtworkUri(albumArt.toUri())
				.build()
		)
		.setUri(pathUri)
		.build()

inline val MediaItem.toSong
	get() = Song(
		mediaId = mediaId,
		name = mediaMetadata.title.toString(),
		artist = mediaMetadata.artist.toString(),
		albumArt = mediaMetadata.artworkUri.toString(),
		pathUri = mediaMetadata.mediaUri.toString()
	)