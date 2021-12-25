package com.looker.data_music

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import com.looker.data_music.utils.MusicCursor
import com.looker.domain_music.Song
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow

class SongsData(private val context: Context) {

	companion object {
		val songsProjections = arrayOf(
			MediaStore.Audio.Media._ID,
			MediaStore.Audio.Media.ALBUM_ID,
			MediaStore.Audio.Genres._ID,
			MediaStore.Audio.Media.TITLE,
			MediaStore.Audio.Media.ARTIST,
			MediaStore.Audio.Media.ALBUM,
			MediaStore.Audio.Media.DURATION
		)
		val externalUri: Uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
		const val sortOrderSong = MediaStore.Audio.Media.TITLE + " COLLATE NOCASE ASC"
	}

	suspend fun createSongsList(): Flow<List<Song>> = flow {
		val list = mutableListOf<Song>()
		getSongFlow().collect { list.add(it) }
		emit(list)
	}


	private fun getSongFlow(): Flow<Song> = flow {

		val songCursor = MusicCursor().generateCursor(context, songsProjections, sortOrderSong)

		songCursor?.let {
			if (it.moveToFirst()) {
				do {
					val songId = it.getLong(0)
					val albumId = it.getLong(1)
					val genreId = it.getLong(2)
					val songName = it.getString(3)
					val artistName = it.getString(4)
					val albumName = it.getString(5)
					val songDurationMillis = it.getLong(6)
					val songUri = "$externalUri/$songId"
					val albumArt = "content://media/external/audio/albumart/$albumId"
					emit(
						Song(
							songUri = songUri,
							albumId = albumId,
							genreId = genreId,
							songName = songName,
							artistName = artistName,
							albumName = albumName,
							duration = songDurationMillis,
							albumArt = albumArt
						)
					)
				} while (it.moveToNext())
			}
			it.close()
		}
	}
}