package com.example.thegroomly.data

import android.content.Context
import com.example.thegroomly.R
import com.example.thegroomly.data.local.AppDatabase
import com.example.thegroomly.data.local.entities.GroomerEntity
import com.example.thegroomly.data.local.entities.ReservationEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class Repository(context: Context) {
    private val db = AppDatabase.get(context)
    private val groomerDao = db.groomerDao()
    private val reservationDao = db.reservationDao()

    // === Flow-ovi za UI ===
    val groomersFlow: Flow<List<GroomerEntity>> = groomerDao.getAll()
    val favoritesFlow: Flow<List<GroomerEntity>> = groomerDao.getFavorites()   // <--- DODANO
    val reservationsFlow: Flow<List<ReservationEntity>> = reservationDao.getAll()

    // Seediraj početne groomere ako su prazni
    suspend fun ensureSeedGroomers() = withContext(Dispatchers.IO) {
        if (groomerDao.count() == 0) {
            groomerDao.insertAll(
                listOf(
                    GroomerEntity(name = "Nera",  distanceMeters = 750,  photoRes = R.drawable.ic_launcher_foreground),
                    GroomerEntity(name = "Doggy", distanceMeters = 1200, photoRes = R.drawable.ic_launcher_foreground),
                    GroomerEntity(name = "Čupko", distanceMeters = 300,  photoRes = R.drawable.ic_launcher_foreground),
                )
            )
        }
    }

    // === Rezervacije ===
    suspend fun upsertReservation(res: ReservationEntity) = withContext(Dispatchers.IO) {
        reservationDao.upsert(res)
    }

    suspend fun updateReservation(res: ReservationEntity) = withContext(Dispatchers.IO) { // <--- POSTOJI
        reservationDao.update(res)
    }

    suspend fun deleteReservation(res: ReservationEntity) = withContext(Dispatchers.IO) {
        reservationDao.delete(res)
    }
}
