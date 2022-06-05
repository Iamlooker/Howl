package com.looker.core_database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.looker.core_model.Album

@Entity(tableName = "albums")
data class AlbumEntity(
	@PrimaryKey
	val albumId: Long,
	val name: String,
	val artist: String,
	val albumArt: String
)

fun AlbumEntity.asExternalModel() = Album(
	albumId = albumId,
	name = name,
	artist = artist,
	albumArt = albumArt
)

fun Album.asEntity() = AlbumEntity(
	albumId = albumId,
	name = name,
	artist = artist,
	albumArt = albumArt
)