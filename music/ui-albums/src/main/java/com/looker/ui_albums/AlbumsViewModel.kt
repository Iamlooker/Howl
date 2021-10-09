package com.looker.ui_albums

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.lifecycle.ViewModel
import com.looker.domain_music.Album
import com.looker.domain_music.Song
import com.looker.domain_music.emptyAlbum
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AlbumsViewModel : ViewModel() {

    private val _songsList = MutableStateFlow(emptyList<Song>())
    val songsList: StateFlow<List<Song>> = _songsList

    private val _currentAlbum = MutableStateFlow(emptyAlbum)
    val currentAlbum: StateFlow<Album> = _currentAlbum

    private val _handleIcon = MutableStateFlow(0.5f)
    val handleIcon: StateFlow<Float> = _handleIcon

    fun onAlbumClick(album: Album) {
        _currentAlbum.value = album
    }

    @ExperimentalMaterialApi
    fun getIcon(state: ModalBottomSheetState) {
        _handleIcon.value = when (state.targetValue) {
            ModalBottomSheetValue.Expanded -> 1f
            ModalBottomSheetValue.HalfExpanded -> 0f
            ModalBottomSheetValue.Hidden -> 0f
        }
    }
}