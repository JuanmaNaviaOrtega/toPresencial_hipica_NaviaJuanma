package com.example.topresencial_hipica_naviajuanma.data.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import com.example.topresencial_hipica_naviajuanma.data.dao.ReservationDao
import com.example.topresencial_hipica_naviajuanma.data.entity.Reservation


@Database(entities = [Reservation::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun reservationDao(): ReservationDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "hipica_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}