package com.example.android.bloodpressurediary.editpressure

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.bloodpressurediary.database.PressureDao

class EditPressureModelFactory(
    private val id: Long,
    private val dao: PressureDao,
    private val application: Application
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EditPressureViewModel::class.java)) {
            return EditPressureViewModel(id, dao, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}