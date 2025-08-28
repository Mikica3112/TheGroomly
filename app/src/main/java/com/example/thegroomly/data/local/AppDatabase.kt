package com.example.thegroomly.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.thegroomly.data.local.dao.GroomerDao
import com.example.thegroomly.data.local.dao.ReservationDao
import com.example.thegroomly.data.local.dao.UserDao
import com.example.thegroomly.data.local.entities.GroomerEntity
import com.example.thegroomly.data.local.entities.ReservationEntity
import com.example.thegroomly.data.local.entities.UserEntity

@Database(
    entities = [
        GroomerEntity::class,
        ReservationEntity::class,
        UserEntity::class
    ],
    version = 2,                 // Povisili smo jer je dodan UserEntity
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun groomerDao(): GroomerDao
    abstract fun reservationDao(): ReservationDao
    abstract fun userDao(): UserDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun get(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "groomly.db"
                )
                    .fallbackToDestructiveMigration() // jednostavno za dev
                    .build()
                INSTANCE = instance
                instance
            }
    }
}
