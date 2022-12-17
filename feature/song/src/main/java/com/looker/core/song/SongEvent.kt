package com.looker.core.song

import com.looker.core.common.order.SongOrder
import com.looker.core.model.Song

sealed class SongEvent {
	data class PlayMedia(val song: Song) : SongEvent()
	data class Order(val songOrder: SongOrder) : SongEvent()
}
