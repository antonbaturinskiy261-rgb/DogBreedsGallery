package com.example.dogbreedsgallery.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface BreedDao {
    @Query("SELECT * FROM breeds ORDER BY name ASC")
    fun observeAll(): Flow<List<BreedEntity>>

    @Query("SELECT * FROM breeds WHERE isFavorite = 1 ORDER BY name ASC")
    fun observeFavorites(): Flow<List<BreedEntity>>

    @Query("SELECT * FROM breeds WHERE name LIKE '%' || :query || '%' ORDER BY name ASC")
    fun search(query: String): Flow<List<BreedEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(breeds: List<BreedEntity>)

    @Query("UPDATE breeds SET isFavorite = :isFavorite WHERE id = :id")
    suspend fun setFavorite(id: Int, isFavorite: Boolean)
}