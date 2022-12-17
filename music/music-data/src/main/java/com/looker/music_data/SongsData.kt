package com.looker.music_data

import android.content.Context
import android.provider.MediaStore
import com.looker.core.model.Song
import com.looker.music_data.utils.MusicCursor
import com.looker.music_data.utils.MusicCursor.externalUri
import kotlinx.coroutines.*

class SongsData(private val context: Context) {

	companion object {
		val songsProjections = arrayOf(
			MediaStore.Audio.AudioColumns._ID,
			MediaStore.Audio.AudioColumns.ALBUM_ID,
			MediaStore.Audio.AudioColumns.TITLE,
			MediaStore.Audio.AudioColumns.ARTIST,
			MediaStore.Audio.AudioColumns.ALBUM,
			MediaStore.Audio.AudioColumns.DURATION
		)
		const val sortOrderSong = MediaStore.Audio.Media.TITLE + " COLLATE NOCASE ASC"
	}

	private val songCursor by lazy {
		MusicCursor.generateCursor(
			context,
			songsProjections,
			sortOrderSong
		)
	}

	suspend fun getAllSongs(): List<Song> {
		val songs = mutableListOf<Deferred<Song>>()
		withContext(Dispatchers.IO) {
			songCursor?.use { cursor ->
				if (cursor.moveToFirst()) {
					do {
						val songId = cursor.getLong(0)
						val albumId = cursor.getLong(1)
						val songName = cursor.getString(2) ?: ""
						val artistName = cursor.getString(3) ?: ""
						val albumName = cursor.getString(4) ?: ""
						val songDurationMillis = cursor.getLong(5)
						val songUri = "$externalUri/$songId"
						val albumArt = "content://media/external/audio/albumart/$albumId"
						val song = async {
							Song(
								mediaId = songId.toString(),
								albumId = albumId,
								pathUri = songUri,
								name = songName,
								artist = artistName,
								album = albumName,
								duration = songDurationMillis,
								albumArt = albumArt
							)
						}
						songs.add(song)
					} while (cursor.moveToNext())
				}
			}
		}
		return songs.awaitAll()
	}
}