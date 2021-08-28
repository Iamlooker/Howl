package com.looker.ui_albums

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.looker.data_albums.data.AlbumsRepository
import com.looker.data_songs.data.SongsRepository

class AlbumsViewModelFactory(
    private val albumsRepository: AlbumsRepository,
    private val songsRepository: SongsRepository
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return AlbumsViewModel(albumsRepository, songsRepository) as T
    }
}