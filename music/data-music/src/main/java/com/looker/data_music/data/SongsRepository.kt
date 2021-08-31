package com.looker.data_music.data

import android.content.Context
import com.looker.data_music.SongsData

class SongsRepository {
    suspend fun getAllSongs(context: Context) = SongsData(context).getSongsList()
}