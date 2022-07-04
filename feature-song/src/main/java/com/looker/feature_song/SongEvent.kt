package com.looker.feature_song

import com.looker.core_common.order.SongOrder
import com.looker.core_model.Song

sealed class SongEvent {
	data class PlayMedia(val song: Song) : SongEvent()
	data class Order(val songOrder: SongOrder) : SongEvent()
}
