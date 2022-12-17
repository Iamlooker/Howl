package com.looker.core.data.use_case

import com.looker.core.common.OrderType
import com.looker.core.common.order.SongOrder
import com.looker.core.model.Song

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
			is SongOrder.Title -> sortedByDescending { it.name }
		}
	}
}
