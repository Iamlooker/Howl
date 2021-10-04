package com.looker.ui_albums

import android.content.Context
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.looker.data_music.data.AlbumsRepository
import com.looker.data_music.data.SongsRepository
import com.looker.domain_music.Album
import com.looker.domain_music.Song
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class AlbumsViewModel(
    private val albumsRepository: AlbumsRepository,
    private val songsRepository: SongsRepository
) : ViewModel() {

    private var allSongs = emptyList<Song>()

    private val _albumsList = MutableStateFlow<List<Album>>(emptyList())
    val albumsList: StateFlow<List<Album>> = _albumsList

    private val _songsList = MutableStateFlow(emptyList<Song>())
    val songsList: StateFlow<List<Song>> = _songsList

    private val _currentAlbum = MutableStateFlow(Album(0, null, null, ""))
    val currentAlbum: StateFlow<Album> = _currentAlbum

    private val _handleIcon = MutableStateFlow(2f)
    val handleIcon: StateFlow<Float> = _handleIcon

    fun onAlbumClick(album: Album) {
        _currentAlbum.value = album
    }

    fun getAllSongs(context: Context) {
        viewModelScope.launch {
            songsRepository.getAllSongs(context).collect { allSongs = it }
        }
    }

    @ExperimentalMaterialApi
    fun getIcon(state: ModalBottomSheetState) {
        _handleIcon.value = when (state.targetValue) {
            ModalBottomSheetValue.Expanded -> 0f
            ModalBottomSheetValue.HalfExpanded -> 2f
            ModalBottomSheetValue.Hidden -> 2f
        }
    }

    fun getAlbumsList(context: Context) {
        viewModelScope.launch {
            albumsRepository.getAllAlbums(context).collect { _albumsList.value = it }
        }
    }

    fun updateSongsList(albumId: Long?) {
        viewModelScope.launch {
            _songsList.value = allSongs.filter { it.albumId == albumId }
        }
    }
}