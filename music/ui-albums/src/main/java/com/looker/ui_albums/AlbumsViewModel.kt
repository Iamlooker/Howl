package com.looker.ui_albums

import android.content.Context
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.material.icons.rounded.ArrowDropUp
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

    @ExperimentalMaterialApi
    fun getIcon(state: ModalBottomSheetState) = when (state.targetValue) {
        ModalBottomSheetValue.Expanded -> Icons.Rounded.ArrowDropDown
        ModalBottomSheetValue.HalfExpanded -> Icons.Rounded.ArrowDropUp
        ModalBottomSheetValue.Hidden -> Icons.Rounded.ArrowDropUp
    }

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