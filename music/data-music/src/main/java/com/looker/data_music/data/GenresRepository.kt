package com.looker.data_music.data

import android.content.Context
import com.looker.data_music.GenresData

class GenresRepository {
    suspend fun getAllGenres(context: Context) = GenresData(context).getGenresList()
}