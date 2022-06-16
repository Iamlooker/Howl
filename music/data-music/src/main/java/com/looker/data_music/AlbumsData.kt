package com.looker.data_music

import android.content.Context
import android.provider.MediaStore
import com.looker.core_model.Album
import com.looker.data_music.utils.MusicCursor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class AlbumsData(private val context: Context) {

	companion object {
		val albumsProjections = arrayOf(
			MediaStore.Audio.Media.ALBUM_ID,
			MediaStore.Audio.Media.ALBUM,
			MediaStore.Audio.Media.ARTIST,
		)
		const val sortOrderAlbum = MediaStore.Audio.Media.ALBUM + " COLLATE NOCASE ASC"
	}

	suspend fun createAlbumsList(): List<Album> {
		val list = mutableListOf<Album>()
		getAlbumFlow().collect { list.add(it) }
		return list
	}

	private fun getAlbumFlow(): Flow<Album> = flow {

		val albumCursor = MusicCursor.generateCursor(context, albumsProjections, sortOrderAlbum)

		albumCursor?.let {
			if (it.moveToFirst()) {
				do {
					val albumId = it.getLong(0)
					val albumName = it.getString(1) ?: ""
					val artistName = it.getString(2) ?: ""
					val albumArt = "content://media/external/audio/albumart/$albumId"
					emit(
						Album(
							albumId = albumId,
							name = albumName,
							artist = artistName,
							albumArt = albumArt
						)
					)
				} while (it.moveToNext())
			}
			it.close()
		}
	}
}