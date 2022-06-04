package com.looker.core_database.dao

import androidx.room.*
import com.looker.core_database.model.SongEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SongDao {

	@Query(
		value = """
        SELECT * FROM songs
        WHERE mediaId = :mediaId
    """
	)
	fun getSongEntityStream(mediaId: String): Flow<SongEntity>

	@Query(value = "SELECT * FROM songs")
	fun getSongEntitiesStream(): Flow<List<SongEntity>>

	@Insert(onConflict = OnConflictStrategy.IGNORE)
	suspend fun insertOrIgnoreSongs(songEntity: List<SongEntity>): List<Long>

	@Update
	suspend fun updateSongs(entities: List<SongEntity>)

	@Transaction
	suspend fun upsertSongs(entities: List<SongEntity>) = upsert(
		items = entities,
		insertMany = ::insertOrIgnoreSongs,
		updateMany = ::updateSongs
	)

	@Query(
		value = """
            DELETE FROM songs
            WHERE mediaId in (:ids)
        """
	)
	suspend fun deleteSongs(ids: List<String>)
}