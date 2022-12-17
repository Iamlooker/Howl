package com.looker.core.database.dao

import androidx.room.*
import com.looker.core.database.model.AlbumEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AlbumDao {

	@Query(
		value = """
        SELECT * FROM albums
        WHERE albumId = :albumId
    """
	)
	fun getAlbumEntityStream(albumId: Long): Flow<AlbumEntity>

	@Query(value = "SELECT * FROM albums")
	fun getAlbumEntitiesStream(): Flow<List<AlbumEntity>>

	@Insert(onConflict = OnConflictStrategy.IGNORE)
	suspend fun insertOrIgnoreAlbums(albumEntity: List<AlbumEntity>): List<Long>

	@Update
	suspend fun updateAlbums(entities: List<AlbumEntity>)

	@Transaction
	suspend fun upsertAlbums(entities: List<AlbumEntity>) = upsert(
		items = entities,
		insertMany = ::insertOrIgnoreAlbums,
		updateMany = ::updateAlbums
	)

	@Query(
		value = """
            DELETE FROM albums
            WHERE albumId in (:ids)
        """
	)
	suspend fun deleteAlbums(ids: List<Long>)
}