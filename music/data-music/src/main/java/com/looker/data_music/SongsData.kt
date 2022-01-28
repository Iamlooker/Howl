package com.looker.data_music

import android.content.Context
import android.provider.MediaStore
import androidx.core.database.getLongOrNull
import com.looker.data_music.utils.MusicCursor
import com.looker.data_music.utils.MusicCursor.externalUri
import com.looker.domain_music.Song
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow

class SongsData(private val context: Context) {

	companion object {
		val songsProjections = arrayOf(
			MediaStore.Audio.AudioColumns._ID,
			MediaStore.Audio.AudioColumns.ALBUM_ID,
			MediaStore.Audio.Genres._ID,
			MediaStore.Audio.AudioColumns.TITLE,
			MediaStore.Audio.AudioColumns.ARTIST,
			MediaStore.Audio.AudioColumns.ALBUM,
			MediaStore.Audio.AudioColumns.DURATION
		)
		const val sortOrderSong = MediaStore.Audio.Media.TITLE + " COLLATE NOCASE ASC"
	}

	suspend fun createSongsList(): List<Song> {
		val song = mutableListOf<Song>()
		getSongFlow().collect { song.add(it) }
		return song
	}

	private fun getSongFlow(): Flow<Song> = flow {

		val songCursor = MusicCursor.generateCursor(context, songsProjections, sortOrderSong)

		songCursor?.let {
			if (it.moveToFirst()) {
				do {
					val songId = it.getLong(0)
					val albumId = it.getLong(1)
					val genreId = it.getLongOrNull(2)
					val songName = it.getString(3)
					val artistName = it.getString(4)
					val albumName = it.getString(5)
					val songDurationMillis = it.getLong(6)
					val songUri = "$externalUri/$songId"
					val albumArt = "content://media/external/audio/albumart/$albumId"
					emit(
						Song(
							mediaId = songId.toString(),
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