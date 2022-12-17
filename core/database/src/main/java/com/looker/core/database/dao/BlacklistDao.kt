package com.looker.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.looker.core.database.model.BlacklistEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BlacklistDao {

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun addBlacklist(blacklist: BlacklistEntity)

	@Query(value = "SELECT * FROM blacklists")
	fun getBlacklistEntitiesStream(): Flow<List<BlacklistEntity>>

	@Update
	suspend fun updateBlacklist(entities: List<BlacklistEntity>)

	@Query(
		value = """
            DELETE FROM blacklists
            WHERE albums in (:ids)
        """
	)
	suspend fun allowAlbum(ids: Set<String>)

	@Query(
		value = """
            DELETE FROM blacklists
            WHERE songs in (:ids)
        """
	)
	suspend fun allowSongs(ids: Set<String>)

	@Query(
		value = """
            DELETE FROM blacklists
            WHERE songsFromAlbum in (:albumIds)
        """
	)
	suspend fun allowAlbumIds(albumIds: Set<String>)
}