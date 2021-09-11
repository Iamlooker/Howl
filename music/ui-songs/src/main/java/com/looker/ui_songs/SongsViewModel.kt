package com.looker.ui_songs

import android.content.Context
import androidx.lifecycle.ViewModel
import com.google.android.exoplayer2.SimpleExoPlayer
import com.looker.data_music.data.Song
import com.looker.data_music.data.SongsRepository
import com.looker.player_service.SimplePlayer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SongsViewModel(private val repository: SongsRepository) : ViewModel() {

    fun buildPlayer(context: Context) = SimpleExoPlayer.Builder(context).build()

    suspend fun getSongsList(context: Context): List<Song> =
        withContext(Dispatchers.IO) { repository.getAllSongs(context) }

    fun playSong(player: SimpleExoPlayer, song: Song) {
        SimplePlayer.playSong(player, song.songUri)
    }
}