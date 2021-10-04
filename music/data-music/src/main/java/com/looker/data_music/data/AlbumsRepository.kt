package com.looker.data_music.data

import android.content.Context
import com.looker.data_music.AlbumsData

class AlbumsRepository {
    suspend fun getAllAlbums(context: Context) = AlbumsData(context).createAlbumsList()
}