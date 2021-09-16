package com.looker.data_music

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import com.looker.data_music.GenresConstants.externalUri
import com.looker.data_music.GenresConstants.genresProjections
import com.looker.data_music.GenresConstants.sortOrderGenre
import com.looker.data_music.data.SongsRepository
import com.looker.domain_music.Genre
import com.looker.domain_music.Song
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow

private object GenresConstants {
    val genresProjections = arrayOf(
        MediaStore.Audio.Genres._ID,
        MediaStore.Audio.Genres.NAME
    )

    val externalUri: Uri = MediaStore.Audio.Genres.EXTERNAL_CONTENT_URI

    const val sortOrderGenre = MediaStore.Audio.Genres.NAME + " COLLATE NOCASE ASC"
}

class GenresData(private val context: Context) {

    suspend fun getGenresList() = createGenresList()

    private suspend fun createGenresList(): List<Genre> {
        val list = mutableListOf<Genre>()
        getGenreFlow().collect { fetchedGenre ->
            list.add(fetchedGenre)
        }
        return list
    }

    private suspend fun getSongsPerGenre(genreId: Long): List<Song> {
        val list = SongsRepository().getAllSongs(context)
        return list.filter { it.genreId == genreId }
    }

    private val genreCursor = context.contentResolver.query(
        externalUri,
        genresProjections,
        null,
        null,
        sortOrderGenre
    )

    private fun getGenreFlow(): Flow<Genre> = flow {


        if (genreCursor?.moveToFirst() == true) {
            do {
                val genreId = genreCursor.getLong(0)
                val genreName = genreCursor.getString(1)
                val songList = getSongsPerGenre(genreId)
                if (genreId != 0) {
                    emit(
                        Genre(
                            genreId, genreName, songList.size
                        )
                    )
                }

            } while (genreCursor.moveToNext())
        }
        genreCursor?.close()
    }
}