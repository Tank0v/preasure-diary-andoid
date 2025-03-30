package com.example.android.bloodpressurediary.pressurelist

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.android.bloodpressurediary.database.Pressure
import com.example.android.bloodpressurediary.database.PressureDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

class PressureListViewModel(
    val dao: PressureDao,
    application: Application
) : AndroidViewModel(application) {
    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val _pressureList = MutableLiveData<MutableList<Pressure>>()
    val pressureList: LiveData<MutableList<Pressure>>
        get() = _pressureList

    init {
        dao.getAllPressure().observeForever { pressureList ->
            _pressureList.value = pressureList.toMutableList()
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}