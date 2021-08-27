package com.looker.ui_albums

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.looker.data_albums.AlbumsData
import com.looker.data_albums.data.Album
import com.looker.data_songs.SongsData
import com.looker.data_songs.data.Song
import kotlinx.coroutines.launch

class AlbumsViewModel(application: Application) : AndroidViewModel(application) {

    private val app = application

    var albumIndex by mutableStateOf(0)

    val currentAlbum
        get() = mutableStateOf(getAlbumsList()[albumIndex])

    fun getAlbumsList(): List<Album> {
        var list by mutableStateOf<List<Album>>(listOf())
        viewModelScope.launch {
            list = AlbumsData(app).getAlbumsList()
        }
        return list.distinct()
    }

    fun getSongsPerAlbum(albumId: Long): List<Song> {
        var list by mutableStateOf<List<Song>>(listOf())
        viewModelScope.launch {
            list = SongsData(app).getSongsList().filter {
                it.albumId == albumId
            }
        }
        return list
    }
}