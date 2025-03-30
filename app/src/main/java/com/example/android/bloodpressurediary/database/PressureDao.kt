package com.example.android.bloodpressurediary.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface PressureDao {
    @Insert
    fun insert(pressure: Pressure) : Long

    @Update
    fun update(pressure: Pressure)

    @Delete
    fun delete(pressure: Pressure)

    @Query("SELECT * FROM pressure_table WHERE id = :id")
    fun get(id: Long) : Pressure?

    @Query("SELECT * FROM pressure_table ORDER BY measurementDateTime DESC")
    fun getAllPressure() : LiveData<List<Pressure>>

    @Query("SELECT * FROM pressure_table WHERE strftime('%H', measurementDateTime) BETWEEN '06' AND '11'")
    fun getPressureMorning() : List<Pressure>

    @Query("SELECT * FROM pressure_table WHERE strftime('%H', measurementDateTime) BETWEEN '12' AND '17'")
    fun getPressureDay() : List<Pressure>

    @Query("SELECT * FROM pressure_table WHERE strftime('%H', measurementDateTime) BETWEEN '18' AND '23'")
    fun getPressureEvening() : List<Pressure>

    @Query("SELECT * FROM pressure_table WHERE strftime('%H', measurementDateTime) BETWEEN '00' AND '05'")
    fun getPressureNight() : List<Pressure>

    @Query("SELECT * FROM pressure_table")
    fun getPressureFullDay() : List<Pressure>
}