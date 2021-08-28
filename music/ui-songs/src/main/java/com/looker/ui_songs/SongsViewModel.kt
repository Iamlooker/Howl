package com.looker.ui_songs

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.looker.data_songs.data.Song
import com.looker.data_songs.data.SongsRepository
import kotlinx.coroutines.launch

class SongsViewModel(private val repository: SongsRepository) : ViewModel() {

    suspend fun getSongsList(context: Context): List<Song> {
        var list = listOf<Song>()
        viewModelScope.launch {
            list = repository.getAllSongs(context)
        }
        return list
    }
}