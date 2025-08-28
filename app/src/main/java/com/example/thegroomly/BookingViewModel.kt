package com.example.thegroomly

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.thegroomly.data.Repository
import com.example.thegroomly.data.local.entities.ReservationEntity
import kotlinx.coroutines.launch

class BookingViewModel(app: Application) : AndroidViewModel(app) {
    private val repo = Repository(app)

    init {
        viewModelScope.launch { repo.ensureSeedGroomers() }
    }

    val groomers = repo.groomersFlow.asLiveData()
    val reservations = repo.reservationsFlow.asLiveData()

    fun reserve(res: ReservationEntity) = viewModelScope.launch {
        repo.upsertReservation(res)
    }

    fun updateReservation(res: ReservationEntity) = viewModelScope.launch {   // <--- DODANO
        repo.updateReservation(res)
    }

    fun delete(res: ReservationEntity) = viewModelScope.launch {
        repo.deleteReservation(res)
    }
}
