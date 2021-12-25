package com.looker.ui_genre

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.looker.data_music.data.GenresRepository

class GenresViewModelFactory(
	private val genreRepository: GenresRepository
) :
	ViewModelProvider.Factory {
	override fun <T : ViewModel?> create(modelClass: Class<T>): T {
		return GenresViewModel(genreRepository) as T
	}
}