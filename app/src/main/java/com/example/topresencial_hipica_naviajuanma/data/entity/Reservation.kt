package com.example.topresencial_hipica_naviajuanma.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Calendar

@Entity(tableName = "reservations")
data class Reservation(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val riderName: String,
    val phone: String,
    val horseName: String,
    val date: String,
    val time: String,
    val comments: String,
    val createdAt: Long = System.currentTimeMillis()
){
    // Función de validación
    fun isValid(): Boolean {
        return try {
            val (day, month, year) = date.split("/")
            val calendar = Calendar.getInstance().apply {
                set(year.toInt(), month.toInt() - 1, day.toInt())
            }
            calendar.get(Calendar.DAY_OF_WEEK) in listOf(Calendar.SATURDAY, Calendar.SUNDAY) &&
                    time in listOf("10:00", "11:00")
        } catch (e: Exception) {
            false
        }
    }
}