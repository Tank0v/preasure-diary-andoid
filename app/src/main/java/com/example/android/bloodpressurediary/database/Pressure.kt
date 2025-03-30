package com.example.android.bloodpressurediary.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "pressure_table")
data class Pressure (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long = 0L,
    @ColumnInfo(name = "measurementDateTime")
    var measurementDateTime: LocalDateTime = LocalDateTime.MIN,

    @ColumnInfo(name = "systolicPressure")
    var systolicPressure: Int = 0,

    @ColumnInfo(name = "diastolicPressure")
    var diastolicPressure: Int = 0,

    @ColumnInfo(name = "pulse")
    var pulse: Int = 0
)