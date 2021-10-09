package com.looker.data_music.data

import android.content.Context
import com.looker.data_music.AlbumsData

class AlbumsRepository(val context: Context) {
    suspend fun getAllAlbums() = AlbumsData(context).createAlbumsList()
}