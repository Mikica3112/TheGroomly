package com.example.thegroomly.data.local.dao

import androidx.room.*
import com.example.thegroomly.data.local.entities.GroomerEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GroomerDao {
    @Query("SELECT * FROM groomers ORDER BY distanceMeters ASC")
    fun getAll(): Flow<List<GroomerEntity>>

    @Query("SELECT * FROM groomers WHERE isFavorite = 1 ORDER BY name ASC")
    fun getFavorites(): Flow<List<GroomerEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<GroomerEntity>)

    @Update
    suspend fun update(item: GroomerEntity)

    @Query("UPDATE groomers SET isFavorite = :favorite WHERE id = :id")
    suspend fun setFavorite(id: Long, favorite: Boolean)

    @Query("SELECT COUNT(*) FROM groomers")
    suspend fun count(): Int
}
