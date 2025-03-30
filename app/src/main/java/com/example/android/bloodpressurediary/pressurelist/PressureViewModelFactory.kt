package com.example.android.bloodpressurediary.pressurelist

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.bloodpressurediary.database.PressureDao

class PressureViewModelFactory(
    private val dao: PressureDao,
    private val application: Application
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PressureListViewModel::class.java)) {
            return PressureListViewModel(dao, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}