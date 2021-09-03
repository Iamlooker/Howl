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
import com.looker.data_music.data.Album
import com.looker.data_music.data.Song
import com.looker.data_music.data.SongsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AlbumsViewModel(
    private val albumsRepository: com.looker.data_music.data.AlbumsRepository,
    private val songsRepository: SongsRepository
) : ViewModel() {

    var currentAlbum by mutableStateOf(Album(0))

    @ExperimentalMaterialApi
    fun getIcon(state: ModalBottomSheetState) = when (state.targetValue) {
        ModalBottomSheetValue.Expanded -> Icons.Rounded.ArrowDropDown
        ModalBottomSheetValue.HalfExpanded -> Icons.Rounded.ArrowDropUp
        ModalBottomSheetValue.Hidden -> Icons.Rounded.ArrowDropUp
    }

    suspend fun getAlbumsList(context: Context): List<Album> =
        withContext(Dispatchers.IO) { albumsRepository.getAllAlbums(context) }

    suspend fun getSongsPerAlbum(context: Context): List<Song> =
        withContext(Dispatchers.IO) {
            songsRepository.getAllSongs(context).filter { it.albumId == currentAlbum.albumId }
        }
}