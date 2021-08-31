package com.looker.ui_songs

import android.content.Context
import androidx.lifecycle.ViewModel
import com.looker.data_music.data.Song
import com.looker.data_music.data.SongsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SongsViewModel(private val repository: SongsRepository) : ViewModel() {

    suspend fun getSongsList(context: Context): List<Song> =
        withContext(Dispatchers.IO) { repository.getAllSongs(context) }
}