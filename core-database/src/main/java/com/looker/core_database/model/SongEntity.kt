package com.looker.core_database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.looker.core_model.Song

@Entity(tableName = "songs")
data class SongEntity(
	@PrimaryKey
	val mediaId: String,
	val albumId: Long,
	val name: String,
	val artist: String,
	val album: String,
	val albumArt: String,
	val pathUri: String,
	val duration: Long
)

fun SongEntity.asExternalModel() = Song(
	mediaId = mediaId,
	albumId = albumId,
	name = name,
	artist = artist,
	album = album,
	albumArt = albumArt,
	pathUri = pathUri,
	duration = duration
)

fun Song.asEntity() = SongEntity(
	mediaId = mediaId,
	albumId = albumId,
	name = name,
	artist = artist,
	album = album,
	albumArt = albumArt,
	pathUri = pathUri,
	duration = duration
)