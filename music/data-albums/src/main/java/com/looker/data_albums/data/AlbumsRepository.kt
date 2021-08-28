package com.looker.data_albums.data

import android.content.Context
import com.looker.data_albums.AlbumsData

class AlbumsRepository {
    suspend fun getAllAlbums(context: Context) = AlbumsData(context).getAlbumsList()
}