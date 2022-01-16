package com.looker.ui_genre

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.looker.data_music.data.GenresRepository
import com.looker.domain_music.Genre
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class GenresViewModel(
	private val genresRepository: GenresRepository
) : ViewModel() {

	private val _genresList = MutableStateFlow<List<Genre>>(listOf())

	val genresList: StateFlow<List<Genre>> = _genresList

	fun getGenreList(context: Context) {
		viewModelScope.launch {
			_genresList.value = genresRepository.getAllGenres(context)
		}
	}
}