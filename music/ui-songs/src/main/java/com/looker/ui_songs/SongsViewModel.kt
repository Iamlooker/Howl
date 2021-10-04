package com.looker.ui_songs

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.looker.data_music.data.SongsRepository
import com.looker.domain_music.Song
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class SongsViewModel(private val repository: SongsRepository) : ViewModel() {

    private val _songsList = MutableStateFlow(emptyList<Song>())
    val songsList: StateFlow<List<Song>> = _songsList

    fun getSongsList(context: Context) {
        viewModelScope.launch {
            repository.getAllSongs(context)
                .collect { list ->
                    _songsList.value = list
                }
        }
    }
}