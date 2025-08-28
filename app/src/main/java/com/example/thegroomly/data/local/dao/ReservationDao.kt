package com.example.thegroomly.data.local.dao

import androidx.room.*
import com.example.thegroomly.data.local.entities.ReservationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ReservationDao {

    @Query("SELECT * FROM reservations ORDER BY dateTime DESC")
    fun getAll(): Flow<List<ReservationEntity>>

    // ovo je "upsert" preko REPLACE
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(reservation: ReservationEntity)

    @Update
    suspend fun update(reservation: ReservationEntity)

    @Delete
    suspend fun delete(reservation: ReservationEntity)
}
