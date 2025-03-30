package com.example.android.bloodpressurediary.editpressure

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
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.time.format.SignStyle
import java.time.temporal.ChronoField
import java.util.Locale

class EditPressureViewModel(
    val id: Long,
    private val pressureDao: PressureDao,
    application: Application
) : AndroidViewModel(application) {
    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private var _measurementDate = MutableLiveData<LocalDate>()
    val measurementDate: LiveData<LocalDate>
        get() = _measurementDate

    private var _measurementTime = MutableLiveData<LocalTime>()
    val measurementTime: LiveData<LocalTime>
        get() = _measurementTime

    private var _systolicPressure = MutableLiveData<Int>()
    val systolicPressure: LiveData<Int>
        get() = _systolicPressure

    private var _diastolicPressure = MutableLiveData<Int>()
    val diastolicPressure: LiveData<Int>
        get() = _diastolicPressure

    private var _pulse = MutableLiveData<Int>()
    val pulse: LiveData<Int>
        get() = _pulse

    private var _errorText = MutableLiveData<String>()
    val errorText: LiveData<String>
        get() = _errorText

    init {
        uiScope.launch {
            val pressure = getPressure()
            pressure?.let {
                _measurementDate.value = it.measurementDateTime.toLocalDate()
                _measurementTime.value = it.measurementDateTime.toLocalTime()
                _systolicPressure.value = it.systolicPressure
                _diastolicPressure.value = it.diastolicPressure
                _pulse.value = it.pulse
            }
        }
    }

    private suspend fun getPressure(): Pressure? {
        return withContext(Dispatchers.IO) {
            pressureDao.get(id)
        }
    }

    fun onEditPressure(
        systolicPressure: String,
        diastolicPressure: String,
        pulse: String
    ) {
        _errorText.value = ""
        val context = getApplication<Application>().applicationContext
        val validatedSystolicPressure = validateSystolicPressure(systolicPressure, context)
        val validatedDiastolicPressure = validateDiastolicPressure(diastolicPressure, context)
        val validatedPulse = validatePulse(pulse, context)
        if (_errorText.value == "") {
            _systolicPressure.value = validatedSystolicPressure
            _diastolicPressure.value = validatedDiastolicPressure
            _pulse.value = validatedPulse

            uiScope.launch {
                updatePressure()
            }
        }
    }

    private suspend fun updatePressure() {
        val pressure = Pressure(
            id,
            LocalDateTime.of(_measurementDate.value, _measurementTime.value),
            _systolicPressure.value ?: 0, _diastolicPressure.value ?: 0,
            _pulse.value ?: 0
        )
        withContext(Dispatchers.IO) {
            pressureDao.update(pressure)
        }
    }

    fun onDeletePressure() {
        uiScope.launch {
            deletePressure()
        }
    }

    private suspend fun deletePressure() {
        val pressure = Pressure(
            id,
            LocalDateTime.of(_measurementDate.value, _measurementTime.value),
            _systolicPressure.value ?: 0, _diastolicPressure.value ?: 0,
            _pulse.value ?: 0
        )
        withContext(Dispatchers.IO) {
            pressureDao.delete(pressure)
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    fun onDateChosen(selectedDate: String) {
        val formatter = DateTimeFormatterBuilder()
            .appendValue(ChronoField.DAY_OF_MONTH, 1, 2, SignStyle.NORMAL)
            .appendLiteral('-')
            .appendValue(ChronoField.MONTH_OF_YEAR, 1, 2, SignStyle.NORMAL)
            .appendLiteral('-')
            .appendValue(ChronoField.YEAR, 4)
            .toFormatter(Locale.getDefault())

        _measurementDate.value = LocalDate.parse(selectedDate, formatter)
    }

    fun onTimeChosen(selectedTime: String) {
        _measurementTime.value = LocalTime.parse(selectedTime, DateTimeFormatter.ISO_LOCAL_TIME)
    }

    private fun validateSystolicPressure(systolicPressure: String, context: Context): Int {
        return try {
            val parsedSystolicPressure = Integer.parseInt(systolicPressure)
            _errorText.value += when {
                parsedSystolicPressure < 40 -> context.getString(R.string.less_systolic_pressure_text)
                parsedSystolicPressure > 250 -> context.getString(R.string.exceed_systolic_pressure_text)
                else -> ""
            }
            parsedSystolicPressure
        } catch (e: NumberFormatException) {
            _errorText.value += context.getString(R.string.not_filled_systolic_pressure_text)
            return -1
        }
    }

    private fun validateDiastolicPressure(diastolicPressure: String, context: Context): Int {
        return try {
            val parsedDiastolicPressure = Integer.parseInt(diastolicPressure)
            _errorText.value += when {
                parsedDiastolicPressure < 20 -> context.getString(R.string.less_diastolic_pressure_text)
                parsedDiastolicPressure > 150 -> context.getString(R.string.exceed_diastolic_pressure_text)
                else -> ""
            }
            parsedDiastolicPressure
        } catch (e: NumberFormatException) {
            _errorText.value += context.getString(R.string.not_filled_diastolic_pressure_text)
            return -1
        }
    }

    private fun validatePulse(pulse: String, context: Context): Int {
        return try {
            val parsedPulse = Integer.parseInt(pulse)
            _errorText.value += when {
                parsedPulse < 30 -> context.getString(R.string.less_pulse_text)
                parsedPulse > 200 -> context.getString(R.string.exceed_pulse_text)
                else -> ""
            }
            parsedPulse
        } catch (e: NumberFormatException) {
            _errorText.value += context.getString(R.string.not_filled_pulse_text)
            return -1
        }
    }
}