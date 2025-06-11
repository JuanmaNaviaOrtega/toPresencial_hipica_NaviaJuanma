package com.example.topresencial_hipica_naviajuanma.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.topresencial_hipica_naviajuanma.data.entity.Reservation


@Dao
interface ReservationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReservation(reservation: Reservation): Long

    @Update
    suspend fun updateReservation(reservation: Reservation): Int

    @Delete
    suspend fun deleteReservation(reservation: Reservation): Int

    @Query("SELECT * FROM reservations ORDER BY date ASC, time ASC")
    fun getAllReservations(): LiveData<List<Reservation>>

    @Query("SELECT * FROM reservations WHERE date = :date AND time = :time")
    fun getReservationsByDateTime(date: String, time: String): LiveData<List<Reservation>>
}