package com.looker.ui_songs

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.exoplayer2.SimpleExoPlayer
import com.looker.data_music.data.Song
import com.looker.data_music.data.SongsRepository
import com.looker.player_service.SimplePlayer
import kotlinx.coroutines.launch

class SongsViewModel(private val repository: SongsRepository) : ViewModel() {

    private val _songsList = MutableLiveData<List<Song>>()

    val songsList: LiveData<List<Song>> = _songsList

    fun buildPlayer(context: Context) = SimpleExoPlayer.Builder(context).build()

    fun getSongsList(context: Context) {
        viewModelScope.launch {
            _songsList.value = repository.getAllSongs(context)
        }
    }

    fun playSong(player: SimpleExoPlayer, song: Song) {
        SimplePlayer.playSong(player, song.songUri)
    }
}