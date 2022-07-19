package com.looker.core_data.use_case

import com.looker.core_common.OrderType
import com.looker.core_common.order.SongOrder
import com.looker.core_model.Song

fun List<Song>.sortBy(songOrder: SongOrder) = when (songOrder.order) {
	OrderType.Ascending -> {
		when (songOrder) {
			is SongOrder.Duration -> sortedBy { it.duration }
			is SongOrder.Title -> sortedBy { it.name }
		}
	}
	OrderType.Descending -> {
		when (songOrder) {
			is SongOrder.Duration -> sortedByDescending { it.duration }
			is SongOrder.Title -> sortedBy { it.name }
		}
	}
}