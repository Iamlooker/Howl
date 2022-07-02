package com.looker.core_data.use_case

import com.looker.core_common.OrderType
import com.looker.core_common.order.SongOrder
import com.looker.core_data.repository.SongsRepository
import com.looker.core_model.Song
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetSongs(private val repository: SongsRepository) {

	operator fun invoke(songOrder: SongOrder): Flow<List<Song>> {
		return repository.getSongsStream().map { songs ->
			when (songOrder.order) {
				OrderType.Ascending -> {
					when (songOrder) {
						is SongOrder.Duration -> songs.sortedBy { it.duration }
						is SongOrder.Title -> songs.sortedBy { it.name }
					}
				}
				OrderType.Descending -> {
					when (songOrder) {
						is SongOrder.Duration -> songs.sortedByDescending { it.duration }
						is SongOrder.Title -> songs.sortedByDescending { it.name }
					}
				}
			}
		}
	}
}