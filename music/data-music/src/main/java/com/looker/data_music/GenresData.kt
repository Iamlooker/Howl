package com.looker.data_music

import android.content.Context
import android.provider.MediaStore
import com.looker.data_music.utils.MusicCursor
import com.looker.domain_music.Genre
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow

class GenresData(private val context: Context) {

	companion object {
		val genresProjections = arrayOf(
			MediaStore.Audio.Genres._ID,
			MediaStore.Audio.Genres.NAME
		)
		const val sortOrderGenre = MediaStore.Audio.Genres.NAME + " COLLATE NOCASE ASC"
	}

	suspend fun getGenresList() = createGenresList()

	private suspend fun createGenresList(): List<Genre> {
		val list = mutableListOf<Genre>()
		getGenreFlow().collect { fetchedGenre ->
			list.add(fetchedGenre)
		}
		return list
	}


	private fun getGenreFlow(): Flow<Genre> = flow {

		val genreCursor = MusicCursor().generateCursor(context, genresProjections, sortOrderGenre)

		genreCursor?.let {
			if (it.moveToFirst()) {
				do {
					val genreId = it.getLong(0)
					val genreName = it.getString(1)
					val songsCount = 0
					emit(Genre(genreId, genreName, songsCount))
				} while (it.moveToNext())
			}
			it.close()
		}
	}
}