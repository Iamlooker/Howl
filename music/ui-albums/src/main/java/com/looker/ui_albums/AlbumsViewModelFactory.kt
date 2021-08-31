package com.looker.ui_albums

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.looker.data_music.data.SongsRepository

class AlbumsViewModelFactory(
    private val albumsRepository: com.looker.data_music.data.AlbumsRepository,
    private val songsRepository: SongsRepository
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return AlbumsViewModel(albumsRepository, songsRepository) as T
    }
}