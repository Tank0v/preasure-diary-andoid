package com.example.android.bloodpressurediary.newpressure

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.bloodpressurediary.database.PressureDao

class NewPressureModelFactory(
    private val dao: PressureDao,
    private val application: Application
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NewPressureViewModel::class.java)) {
            return NewPressureViewModel(dao, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}