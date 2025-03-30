package com.example.android.bloodpressurediary.statistics

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.android.bloodpressurediary.R
import com.example.android.bloodpressurediary.database.PressureDatabase
import com.example.android.bloodpressurediary.databinding.FragmentStatisticsBinding

class StatisticsFragment : Fragment() {
    private lateinit var viewModel: StatisticsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentStatisticsBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_statistics, container, false
        )
        val application = requireNotNull(this.activity).application
        val dao = PressureDatabase.getInstance(application).getPressureDatabaseDao()
        val viewModelFactory = StatisticsViewModelFactory(dao, application)
        viewModel = ViewModelProvider(this, viewModelFactory).get(StatisticsViewModel::class.java)

        viewModel.morningStartEndDate.observe(viewLifecycleOwner, Observer { date ->
            binding.morningStartEndPeriodTextview.text = date
        })

        viewModel.morningAveragePressure.observe(viewLifecycleOwner, Observer { pressure ->
            binding.morningAveragePressureTextview.text = pressure
        })

        viewModel.morningMaxPressure.observe(viewLifecycleOwner, Observer { pressure ->
            binding.morningMaxPressureTextview.text = pressure
        })

        viewModel.morningMinPressure.observe(viewLifecycleOwner, Observer { pressure ->
            binding.morningMinPressureTextview.text = pressure
        })

        viewModel.dayStartEndDate.observe(viewLifecycleOwner, Observer { date ->
            binding.dayStartEndPeriodTextview.text = date
        })

        viewModel.dayAveragePressure.observe(viewLifecycleOwner, Observer { pressure ->
            binding.dayAveragePressureTextview.text = pressure
        })

        viewModel.dayMaxPressure.observe(viewLifecycleOwner, Observer { pressure ->
            binding.dayMaxPressureTextview.text = pressure
        })

        viewModel.dayMinPressure.observe(viewLifecycleOwner, Observer { pressure ->
            binding.dayMinPressureTextview.text = pressure
        })

        viewModel.eveningStartEndDate.observe(viewLifecycleOwner, Observer { date ->
            binding.eveningStartEndPeriodTextview.text = date
        })

        viewModel.eveningAveragePressure.observe(viewLifecycleOwner, Observer { pressure ->
            binding.eveningAveragePressureTextview.text = pressure
        })

        viewModel.eveningMaxPressure.observe(viewLifecycleOwner, Observer { pressure ->
            binding.eveningMaxPressureTextview.text = pressure
        })

        viewModel.eveningMinPressure.observe(viewLifecycleOwner, Observer { pressure ->
            binding.eveningMinPressureTextview.text = pressure
        })

        viewModel.nightStartEndDate.observe(viewLifecycleOwner, Observer { date ->
            binding.nightStartEndPeriodTextview.text = date
        })

        viewModel.nightAveragePressure.observe(viewLifecycleOwner, Observer { pressure ->
            binding.nightAveragePressureTextview.text = pressure
        })

        viewModel.nightMaxPressure.observe(viewLifecycleOwner, Observer { pressure ->
            binding.nightMaxPressureTextview.text = pressure
        })

        viewModel.nightMinPressure.observe(viewLifecycleOwner, Observer { pressure ->
            binding.nightMinPressureTextview.text = pressure
        })

        viewModel.fullDayStartEndDate.observe(viewLifecycleOwner, Observer { date ->
            binding.fullDayStartEndPeriodTextview.text = date
        })

        viewModel.fullDayAveragePressure.observe(viewLifecycleOwner, Observer { pressure ->
            binding.fullDayAveragePressureTextview.text = pressure
        })

        viewModel.fullDayMaxPressure.observe(viewLifecycleOwner, Observer { pressure ->
            binding.fullDayMaxPressureTextview.text = pressure
        })

        viewModel.fullDayMinPressure.observe(viewLifecycleOwner, Observer { pressure ->
            binding.fullDayMinPressureTextview.text = pressure
        })
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as? AppCompatActivity)?.supportActionBar?.title = getString(R.string.statistics_fragment_title)
    }
}