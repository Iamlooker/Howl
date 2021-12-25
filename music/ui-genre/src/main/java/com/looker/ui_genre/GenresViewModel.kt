package com.looker.ui_genre

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.looker.data_music.data.GenresRepository
import com.looker.domain_music.Genre
import kotlinx.coroutines.launch

class GenresViewModel(
	private val genresRepository: GenresRepository
) : ViewModel() {

	private val _genresList = MutableLiveData<List<Genre>>()

	val genresList: LiveData<List<Genre>> = _genresList

	fun getGenreList(context: Context) {
		viewModelScope.launch {
			_genresList.value = genresRepository.getAllGenres(context)
		}
	}
}