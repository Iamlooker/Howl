package com.looker.ui_albums

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.looker.data_albums.data.Album
import com.looker.data_albums.data.AlbumsRepository
import com.looker.data_songs.data.Song
import com.looker.data_songs.data.SongsRepository
import kotlinx.coroutines.launch

class AlbumsViewModel(
    private val albumsRepository: AlbumsRepository,
    private val songsRepository: SongsRepository
) : ViewModel() {

    val currentAlbum = mutableStateOf(Album(0))

    fun getAlbumsList(context: Context): List<Album> {
        var list by mutableStateOf<List<Album>>(listOf())
        viewModelScope.launch {
            list = albumsRepository.getAllAlbums(context)
        }
        return list.distinct()
    }

    fun getSongsPerAlbum(context: Context, albumId: Long): List<Song> {
        var list by mutableStateOf<List<Song>>(listOf())
        viewModelScope.launch {
            list = songsRepository.getAllSongs(context).filter {
                it.albumId == albumId
            }
        }
        return list
    }
}