package com.example.android.bloodpressurediary.newpressure

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.android.bloodpressurediary.R
import com.example.android.bloodpressurediary.database.PressureDatabase
import com.example.android.bloodpressurediary.databinding.FragmentNewPressureBinding
import java.util.Calendar

class NewPressureFragment : Fragment() {
    private lateinit var viewModel: NewPressureViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentNewPressureBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_new_pressure, container, false
        )
        val application = requireNotNull(this.activity).application
        val dao = PressureDatabase.getInstance(application).getPressureDatabaseDao()
        val viewModelFactory = NewPressureModelFactory(dao, application)
        viewModel = ViewModelProvider(this, viewModelFactory).get(NewPressureViewModel::class.java)

        binding.newPressureButton.setOnClickListener {
            viewModel.onAddPressure(
                binding.systolicPressureEdittext.text.toString(),
                binding.diastolicPressureEdittext.text.toString(),
                binding.pulseEdittext.text.toString()
            )
            if (viewModel.errorText.value == "") {
                Navigation.findNavController(it)
                    .navigate(R.id.action_newPressureFragment_to_pressureListFragment)
            }
        }

        binding.measurementDateButton.setOnClickListener {
            showDatePicker()
        }

        binding.measurementTimeButton.setOnClickListener {
            showTimePicker()
        }

        viewModel.measurementDate.observe(viewLifecycleOwner, Observer { date ->
            binding.chosenMeasurementDateTextview.text = date.toString()
        })

        viewModel.measurementTime.observe(viewLifecycleOwner, Observer { time ->
            binding.chosenMeasurementTimeTextview.text = time.toString()
        })

        viewModel.errorText.observe(viewLifecycleOwner, Observer { error ->
            binding.errorTextview.text = error
        })

        return binding.root
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(), { _, selectedYear, selectedMonth, selectedDay ->
                val selectedDate = "$selectedDay-${selectedMonth + 1}-$selectedYear"
                viewModel.onDateChosen(selectedDate)
            }, year, month, day
        )
        datePickerDialog.show()
    }

    private fun showTimePicker() {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(
            requireContext(), { _, selectedHour, selectedMinute ->
                val selectedTime = String.format("%02d:%02d", selectedHour, selectedMinute)
                viewModel.onTimeChosen(selectedTime)
            }, hour, minute, true
        )
        timePickerDialog.show()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as? AppCompatActivity)?.supportActionBar?.title = getString(R.string.new_pressure_fragment_title)
    }
}