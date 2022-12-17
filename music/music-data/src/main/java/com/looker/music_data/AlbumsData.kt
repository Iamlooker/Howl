package com.looker.music_data

import android.content.Context
import android.provider.MediaStore
import com.looker.core_model.Album
import com.looker.music_data.utils.MusicCursor
import kotlinx.coroutines.*

class AlbumsData(private val context: Context) {

	companion object {
		val albumsProjections = arrayOf(
			MediaStore.Audio.Albums.ALBUM_ID,
			MediaStore.Audio.Albums.ALBUM,
			MediaStore.Audio.Albums.ARTIST
		)
		const val sortOrderAlbum = MediaStore.Audio.Albums.ALBUM + " COLLATE NOCASE ASC"
	}

	private val albumCursor by lazy {
		MusicCursor.generateCursor(
			context,
			albumsProjections,
			sortOrderAlbum
		)
	}

	suspend fun getAllAlbums(): List<Album> {
		val albums = mutableListOf<Deferred<Album>>()
		withContext(Dispatchers.IO) {
			albumCursor?.use { cursor ->
				if (cursor.moveToFirst()) {
					do {
						val albumId = cursor.getLong(0)
						val albumName = cursor.getString(1) ?: ""
						val artistName = cursor.getString(2) ?: ""
						val albumArt = "content://media/external/audio/albumart/$albumId"
						val album = async {
							Album(
								albumId = albumId,
								name = albumName,
								artist = artistName,
								albumArt = albumArt,
							)
						}
						albums.add(album)
					} while (cursor.moveToNext())
				}
			}
		}
		return albums.awaitAll()
	}
}