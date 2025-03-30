package com.example.android.bloodpressurediary.statistics

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.android.bloodpressurediary.R
import com.example.android.bloodpressurediary.database.Pressure
import com.example.android.bloodpressurediary.database.PressureDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class StatisticsViewModel(
    private val pressureDao: PressureDao,
    application: Application
) : AndroidViewModel(application) {
    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private var _pressureList = MutableLiveData<MutableList<Pressure>>()
    val pressureList: LiveData<MutableList<Pressure>>
        get() = _pressureList

    private var _morningStartEndDate = MutableLiveData<String>()
    val morningStartEndDate: LiveData<String>
        get() = _morningStartEndDate

    private var _morningAveragePressure = MutableLiveData<String>()
    val morningAveragePressure: LiveData<String>
        get() = _morningAveragePressure

    private var _morningMaxPressure = MutableLiveData<String>()
    val morningMaxPressure: LiveData<String>
        get() = _morningMaxPressure

    private var _morningMinPressure = MutableLiveData<String>()
    val morningMinPressure: LiveData<String>
        get() = _morningMinPressure

    private var _dayStartEndDate = MutableLiveData<String>()
    val dayStartEndDate: LiveData<String>
        get() = _dayStartEndDate

    private var _dayAveragePressure = MutableLiveData<String>()
    val dayAveragePressure: LiveData<String>
        get() = _dayAveragePressure

    private var _dayMaxPressure = MutableLiveData<String>()
    val dayMaxPressure: LiveData<String>
        get() = _dayMaxPressure

    private var _dayMinPressure = MutableLiveData<String>()
    val dayMinPressure: LiveData<String>
        get() = _dayMinPressure

    private var _eveningStartEndDate = MutableLiveData<String>()
    val eveningStartEndDate: LiveData<String>
        get() = _eveningStartEndDate

    private var _eveningAveragePressure = MutableLiveData<String>()
    val eveningAveragePressure: LiveData<String>
        get() = _eveningAveragePressure

    private var _eveningMaxPressure = MutableLiveData<String>()
    val eveningMaxPressure: LiveData<String>
        get() = _eveningMaxPressure

    private var _eveningMinPressure = MutableLiveData<String>()
    val eveningMinPressure: LiveData<String>
        get() = _eveningMinPressure

    private var _nightStartEndDate = MutableLiveData<String>()
    val nightStartEndDate: LiveData<String>
        get() = _nightStartEndDate

    private var _nightAveragePressure = MutableLiveData<String>()
    val nightAveragePressure: LiveData<String>
        get() = _nightAveragePressure

    private var _nightMaxPressure = MutableLiveData<String>()
    val nightMaxPressure: LiveData<String>
        get() = _nightMaxPressure

    private var _nightMinPressure = MutableLiveData<String>()
    val nightMinPressure: LiveData<String>
        get() = _nightMinPressure

    private var _fullDayStartEndDate = MutableLiveData<String>()
    val fullDayStartEndDate: LiveData<String>
        get() = _fullDayStartEndDate

    private var _fullDayAveragePressure = MutableLiveData<String>()
    val fullDayAveragePressure: LiveData<String>
        get() = _fullDayAveragePressure

    private var _fullDayMaxPressure = MutableLiveData<String>()
    val fullDayMaxPressure: LiveData<String>
        get() = _fullDayMaxPressure

    private var _fullDayMinPressure = MutableLiveData<String>()
    val fullDayMinPressure: LiveData<String>
        get() = _fullDayMinPressure

    init {
        uiScope.launch {
            val context = getApplication<Application>().applicationContext
            var pressure = getPressureMorning()
            _pressureList.value = pressure.toMutableList()
            setStatistics(
                context,
                _morningStartEndDate,
                _morningAveragePressure,
                _morningMaxPressure,
                _morningMinPressure
            )
            pressure = getPressureDay()
            _pressureList.value = pressure.toMutableList()
            setStatistics(
                context,
                _dayStartEndDate,
                _dayAveragePressure,
                _dayMaxPressure,
                _dayMinPressure
            )
            pressure = getPressureEvening()
            _pressureList.value = pressure.toMutableList()
            setStatistics(
                context,
                _eveningStartEndDate,
                _eveningAveragePressure,
                _eveningMaxPressure,
                _eveningMinPressure
            )
            pressure = getPressureNight()
            _pressureList.value = pressure.toMutableList()
            setStatistics(
                context,
                _nightStartEndDate,
                _nightAveragePressure,
                _nightMaxPressure,
                _nightMinPressure
            )
            pressure = getPressureFullDay()
            _pressureList.value = pressure.toMutableList()
            setStatistics(
                context,
                _fullDayStartEndDate,
                _fullDayAveragePressure,
                _fullDayMaxPressure,
                _fullDayMinPressure
            )
        }
    }

    private fun setStatistics(
        context: Context,
        startEndDate: MutableLiveData<String>,
        averagePressure: MutableLiveData<String>,
        maxPressure: MutableLiveData<String>,
        minPressure: MutableLiveData<String>
    ) {
        setStartEndDate(startEndDate, context)
        setAveragePressure(averagePressure, context)
        setMaxPressure(maxPressure, context)
        setMinPressure(minPressure, context)
    }

    private fun setStartEndDate(startEndDate: MutableLiveData<String>, context: Context) {
        val minDateTime =
            _pressureList.value?.minByOrNull { it.measurementDateTime }?.measurementDateTime
        if (minDateTime == null) {
            startEndDate.value = context.getString(R.string.no_data_text)
            return
        }
        val minDate = minDateTime.toLocalDate().toString()
        val maxDateTime =
            _pressureList.value?.maxByOrNull { it.measurementDateTime }?.measurementDateTime
        val maxDate = maxDateTime?.toLocalDate().toString()
        startEndDate.value =
            "${context.getString(R.string.start_date_text)} $minDate. ${context.getString(R.string.end_date_text)} $maxDate."
    }

    private suspend fun getPressureMorning(): List<Pressure> {
        return withContext(Dispatchers.IO) {
            pressureDao.getPressureMorning()
        }
    }

    private suspend fun getPressureDay(): List<Pressure> {
        return withContext(Dispatchers.IO) {
            pressureDao.getPressureDay()
        }
    }

    private suspend fun getPressureEvening(): List<Pressure> {
        return withContext(Dispatchers.IO) {
            pressureDao.getPressureEvening()
        }
    }

    private suspend fun getPressureNight(): List<Pressure> {
        return withContext(Dispatchers.IO) {
            pressureDao.getPressureNight()
        }
    }

    private suspend fun getPressureFullDay(): List<Pressure> {
        return withContext(Dispatchers.IO) {
            pressureDao.getPressureFullDay()
        }
    }

    private fun setAveragePressure(averagePressure: MutableLiveData<String>, context: Context) {
        val averageSystolicPressure = _pressureList.value?.map { it.systolicPressure }?.average()
        if (averageSystolicPressure != null) {
            if (averageSystolicPressure.isNaN()) {
                averagePressure.value = context.getString(R.string.no_data_text)
                return
            }
        }
        val averageDiastolicPressure = _pressureList.value?.map { it.diastolicPressure }?.average()
        val averagePulse = _pressureList.value?.map { it.pulse }?.average()

        val formattedSystolic = String.format("%.3f", averageSystolicPressure ?: 0.0)
        val formattedDiastolic = String.format("%.3f", averageDiastolicPressure ?: 0.0)
        val formattedPulse = String.format("%.3f", averagePulse ?: 0.0)

        averagePressure.value =
            "${context.getString(R.string.average_systolic_text)} $formattedSystolic. ${
                context.getString(
                    R.string.average_diastolic_text
                )
            } $formattedDiastolic. ${context.getString(R.string.average_pulse_text)} $formattedPulse."
    }

    private fun setMinPressure(minPressure: MutableLiveData<String>, context: Context) {
        val minSystolicPressure =
            _pressureList.value?.minByOrNull { it.systolicPressure }?.systolicPressure
        if (minSystolicPressure == null) {
            minPressure.value = context.getString(R.string.no_data_text)
            return
        }
        val minDiastolicPressure =
            _pressureList.value?.minByOrNull { it.diastolicPressure }?.diastolicPressure
        val minPulse = _pressureList.value?.minByOrNull { it.pulse }?.pulse
        minPressure.value =
            "${context.getString(R.string.min_systolic_text)} $minSystolicPressure. ${
                context.getString(
                    R.string.min_diastolic_text
                )
            } $minDiastolicPressure. ${context.getString(R.string.min_pulse_text)} $minPulse."
    }

    private fun setMaxPressure(maxPressure: MutableLiveData<String>, context: Context) {
        val maxSystolicPressure =
            _pressureList.value?.maxByOrNull { it.systolicPressure }?.systolicPressure
        if (maxSystolicPressure == null) {
            maxPressure.value = context.getString(R.string.no_data_text)
            return
        }
        val maxDiastolicPressure =
            _pressureList.value?.maxByOrNull { it.diastolicPressure }?.diastolicPressure
        val maxPulse = _pressureList.value?.maxByOrNull { it.pulse }?.pulse
        maxPressure.value =
            "${context.getString(R.string.max_systolic_text)} $maxSystolicPressure. ${
                context.getString(
                    R.string.max_diastolic_text
                )
            } $maxDiastolicPressure. ${context.getString(R.string.max_pulse_text)} $maxPulse."
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}