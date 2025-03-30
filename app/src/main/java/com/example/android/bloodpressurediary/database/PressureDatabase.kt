package com.example.android.bloodpressurediary.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.android.bloodpressurediary.utils.DateTimeConverters

@Database(entities = [Pressure::class], version = 1, exportSchema = false)
@TypeConverters(DateTimeConverters::class)
abstract class PressureDatabase : RoomDatabase() {
    abstract fun getPressureDatabaseDao(): PressureDao

    companion object {
        @Volatile
        private var INSTANCE: PressureDatabase? = null

        fun getInstance(context: Context): PressureDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(context.applicationContext,
                        PressureDatabase::class.java, "pressure_db")
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}