package com.looker.ui_albums

import android.content.Context
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.looker.data_music.data.AlbumsRepository
import com.looker.data_music.data.SongsRepository
import com.looker.domain_music.Album
import com.looker.domain_music.Song
import kotlinx.coroutines.launch

class AlbumsViewModel(
    private val albumsRepository: AlbumsRepository,
    private val songsRepository: SongsRepository
) : ViewModel() {

    private val _albumsList = MutableLiveData<List<Album>>()
    private val _songsList = MutableLiveData<List<Song>>()
    private val _currentAlbum = MutableLiveData<Album>()
    private val _handleIcon = MutableLiveData<Float>()
    private var allSongsList = listOf<Song>()

    val albumsList: LiveData<List<Album>> = _albumsList
    val songsList: LiveData<List<Song>> = _songsList
    val currentAlbum: LiveData<Album> = _currentAlbum
    val handleIcon: LiveData<Float> = _handleIcon

    @ExperimentalMaterialApi
    suspend fun onAlbumClick(state: ModalBottomSheetState, album: Album) {
        _currentAlbum.value = album
        state.show()
    }

    fun getAllSongs(context: Context) {
        viewModelScope.launch {
            allSongsList = songsRepository.getAllSongs(context)
        }
    }

    @ExperimentalMaterialApi
    fun getIcon(state: ModalBottomSheetState) {
        _handleIcon.value = when (state.targetValue) {
            ModalBottomSheetValue.Expanded -> 2f
            ModalBottomSheetValue.HalfExpanded -> 0f
            ModalBottomSheetValue.Hidden -> 0f
        }
    }

    fun getAlbumsList(context: Context) {
        viewModelScope.launch { _albumsList.value = albumsRepository.getAllAlbums(context) }
    }

    fun getSongsPerAlbum() {
        viewModelScope.launch {
            _songsList.value = allSongsList.filter { it.albumId == _currentAlbum.value?.albumId }
        }
    }
}