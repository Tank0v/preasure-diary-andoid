package com.example.android.bloodpressurediary.pressurelist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.android.bloodpressurediary.R
import com.example.android.bloodpressurediary.database.PressureDatabase
import com.example.android.bloodpressurediary.databinding.FragmentPressureListBinding

class PressureListFragment : Fragment(), OnItemClickListener {
    private lateinit var viewModel: PressureListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentPressureListBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_pressure_list, container, false
        )
        val application = requireNotNull(this.activity).application
        val dao = PressureDatabase.getInstance(application).getPressureDatabaseDao()
        val viewModelFactory = PressureViewModelFactory(dao, application)
        viewModel = ViewModelProvider(this, viewModelFactory).get(PressureListViewModel::class.java)

        val adapter = PressureListAdapter(this)
        binding.pressureList.adapter = adapter

        viewModel.pressureList.observe(viewLifecycleOwner, Observer { pressure ->
            adapter.data = pressure
        })

        binding.addPressureButton.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_pressureListFragment_to_newPressureFragment)
        }

        binding.checkStatisticsButton.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_pressureListFragment_to_statisticsFragment)
        }

        return binding.root
    }


    override fun onResume() {
            super.onResume()
            (activity as? AppCompatActivity)?.supportActionBar?.title = getString(R.string.pressure_list_fragment_title)
        }

    override fun onItemClick(id: Long) {
        val bundle = bundleOf("id" to id)
        Navigation.findNavController(this.requireView()).navigate(R.id.action_pressureListFragment_to_editPressureFragment, bundle)
    }
}