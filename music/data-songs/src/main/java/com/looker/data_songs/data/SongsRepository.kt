package com.looker.data_songs.data

import android.content.Context
import com.looker.data_songs.SongsData

class SongsRepository {
    suspend fun getAllSongs(context: Context) = SongsData(context).getSongsList()
}