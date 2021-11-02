package cz.johnyapps.cheers.fragments

import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import cz.johnyapps.cheers.R
import cz.johnyapps.cheers.databinding.FragmentStatisticsBinding
import cz.johnyapps.cheers.entities.CounterWithBeverage
import cz.johnyapps.cheers.entities.counter.CounterEntry
import cz.johnyapps.cheers.repositories.BeverageRepository
import cz.johnyapps.cheers.viewmodels.StatisticsViewModel
import java.util.*

class StatisticsFragment: Fragment() {
    private lateinit var viewModel: StatisticsViewModel
    private lateinit var binding: FragmentStatisticsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val provider = ViewModelProvider(this)
        viewModel = provider.get(StatisticsViewModel::class.java)

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_statistics, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        binding.valueEditLayout.setOnClickListener {
            changeTime()
        }

        setupObservers()

        return binding.root
    }

    private fun setupObservers() {
        viewModel.counters.observe(viewLifecycleOwner, {
            binding.graphView.setGraphValueSets(it)
            binding.graphView.selectValue(viewModel.selectedGraphValue.value)
        })
    }

    private fun changeTime() {
        val counterWithBeverage = viewModel.selectedGraphValueSet.value
        val counterEntry = viewModel.selectedGraphValue.value

        if (counterEntry is CounterEntry && counterWithBeverage is CounterWithBeverage) {
            val calendar = Calendar.getInstance()
            calendar.time = counterEntry.time

            val dialog = TimePickerDialog(binding.root.context,
                { _, hourOfDay, minute ->
                    changeTime(hourOfDay, minute, counterEntry, counterWithBeverage) },
                calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE),
                true)
            dialog.show()
        }
    }

    private fun changeTime(hour: Int,
                           minute: Int,
                           counterEntry: CounterEntry,
                           counterWithBeverage: CounterWithBeverage) {
        val calendar = Calendar.getInstance()
        calendar.time = counterEntry.time
        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, minute)
        counterEntry.time = calendar.time

        val repository = BeverageRepository(requireContext(), lifecycleScope)
        repository.updateCounter(counterWithBeverage.counter)
    }
}