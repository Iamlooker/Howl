package com.looker.data_albums.data

import com.looker.core.domain.FilterOrder

sealed class FilterAlbums(val filterType: String) {

    data class AlbumNameDescending(
        val order: FilterOrder = FilterOrder.Descending
    ) : FilterAlbums("Name: Z-A")

    data class AlbumNameAscending(
        val order: FilterOrder = FilterOrder.Ascending
    ) : FilterAlbums("Name: A-Z")

}