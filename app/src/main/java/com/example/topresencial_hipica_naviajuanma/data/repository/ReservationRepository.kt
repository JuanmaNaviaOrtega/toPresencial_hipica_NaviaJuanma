package com.example.topresencial_hipica_naviajuanma.data.repository

import androidx.lifecycle.LiveData
import com.example.topresencial_hipica_naviajuanma.data.dao.ReservationDao
import com.example.topresencial_hipica_naviajuanma.data.entity.Reservation


class ReservationRepository(private val reservationDao: ReservationDao) {
    val allReservations: LiveData<List<Reservation>> = reservationDao.getAllReservations()

    suspend fun insert(reservation: Reservation): Long {
        return reservationDao.insertReservation(reservation)
    }

    suspend fun update(reservation: Reservation): Int {
        return reservationDao.updateReservation(reservation)
    }

    suspend fun delete(reservation: Reservation): Int {
        return reservationDao.deleteReservation(reservation)
    }

    fun getReservationsByDateTime(date: String, time: String): LiveData<List<Reservation>> {
        return reservationDao.getReservationsByDateTime(date, time)
    }
}