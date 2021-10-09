package com.looker.data_music.data

import android.content.Context
import com.looker.data_music.SongsData

class SongsRepository(val context: Context) {
    suspend fun getAllSongs() = SongsData(context).createSongsList()
}