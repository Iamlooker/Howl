package com.looker.music_data

import android.content.Context
import android.provider.MediaStore
import com.looker.core_model.Album
import com.looker.music_data.utils.MusicCursor
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

class AlbumsData(private val context: Context) {

	companion object {
		val albumsProjections = arrayOf(
			MediaStore.Audio.Albums.ALBUM_ID,
			MediaStore.Audio.Albums.ALBUM,
			MediaStore.Audio.Albums.ARTIST,
			MediaStore.Audio.Albums.NUMBER_OF_SONGS
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
		coroutineScope {
			albumCursor?.let { cursor ->
				if (cursor.moveToFirst()) {
					do {
						val albumId = cursor.getLong(0)
						val albumName = cursor.getString(1) ?: ""
						val artistName = cursor.getString(2) ?: ""
						val albumArt = "content://media/external/audio/albumart/$albumId"
						val numberOfSongs = cursor.getInt(3)
						val album = async {
							Album(
								albumId = albumId,
								name = albumName,
								artist = artistName,
								albumArt = albumArt,
								numberOfSongs = numberOfSongs
							)
						}
						albums.add(album)
					} while (cursor.moveToNext())
				}
				cursor.close()
			}
		}
		return albums.awaitAll()
	}
}