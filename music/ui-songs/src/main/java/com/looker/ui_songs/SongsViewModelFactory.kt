package com.looker.ui_songs

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.looker.data_songs.data.SongsRepository

class SongsViewModelFactory(private val repository: SongsRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SongsViewModel(repository) as T
    }
}