package com.looker.ui_albums

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.looker.data_albums.AlbumsData
import com.looker.data_albums.data.Album
import kotlinx.coroutines.launch

class AlbumsViewModel(application: Application) : AndroidViewModel(application) {

    private val app = application

    fun getAlbumsList(): List<Album> {
        var list by mutableStateOf<List<Album>>(listOf())
        viewModelScope.launch {
            list = AlbumsData(app).getAlbumsList()
        }
        return list.distinct()
    }
}