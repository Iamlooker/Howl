package com.looker.core_database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.looker.core_model.Blacklist

@Entity(tableName = "blacklists")
data class BlacklistEntity(
	@PrimaryKey(autoGenerate = true)
	val id: Long = 0L,
	val songs: Set<String> = emptySet(),
	val albums: Set<String> = emptySet(),
	val songsFromAlbum: Set<String> = emptySet()
)

fun Blacklist.asEntity() = BlacklistEntity(
	songs = songs,
	albums = albums,
	songsFromAlbum = songsFromAlbum
)

fun BlacklistEntity.asExternalModel() = Blacklist(
	songs = songs,
	albums = albums,
	songsFromAlbum = songsFromAlbum
)