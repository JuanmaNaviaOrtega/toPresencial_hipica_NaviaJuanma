package com.example.topresencial_hipica_naviajuanma.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope

import com.example.topresencial_hipica_naviajuanma.data.database.AppDatabase
import com.example.topresencial_hipica_naviajuanma.data.entity.Reservation
import com.example.topresencial_hipica_naviajuanma.data.repository.ReservationRepository
import kotlinx.coroutines.launch

class ReservationViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: ReservationRepository
    val allReservations: LiveData<List<Reservation>>

    init {
        val reservationDao = AppDatabase.getDatabase(application).reservationDao()
        repository = ReservationRepository(reservationDao)
        allReservations = repository.allReservations
    }

    fun insert(reservation: Reservation) = viewModelScope.launch {
        repository.insert(reservation)
    }

    fun update(reservation: Reservation) = viewModelScope.launch {
        repository.update(reservation)
    }

    fun delete(reservation: Reservation) = viewModelScope.launch {
        repository.delete(reservation)
    }

    fun getReservationsByDateTime(date: String, time: String): LiveData<List<Reservation>> {
        return repository.getReservationsByDateTime(date, time)
    }
}