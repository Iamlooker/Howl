package com.looker.ui_songs

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.looker.data_songs.SongsData
import com.looker.data_songs.data.Song
import kotlinx.coroutines.launch

class SongsViewModel(application: Application) : AndroidViewModel(application) {

    private val app = application

    fun getSongsList(): List<Song> {
        var list by mutableStateOf<List<Song>>(listOf())
        viewModelScope.launch {
            list = SongsData(app).getSongsList()
        }
        return list
    }
}