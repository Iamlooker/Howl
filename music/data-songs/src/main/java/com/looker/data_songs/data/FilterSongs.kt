package com.looker.data_songs.data

import com.looker.core.domain.FilterOrder

sealed class FilterSongs(val filterType: String) {

    data class SongNameDescending(
        val order: FilterOrder = FilterOrder.Descending
    ) : FilterSongs("Name: Z-A")

    data class SongNameAscending(
        val order: FilterOrder = FilterOrder.Ascending
    ) : FilterSongs("Name: A-Z")

}