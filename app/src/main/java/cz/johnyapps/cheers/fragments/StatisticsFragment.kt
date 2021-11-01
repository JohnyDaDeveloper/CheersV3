package cz.johnyapps.cheers.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import cz.johnyapps.cheers.R
import cz.johnyapps.cheers.databinding.FragmentStatisticsBinding
import cz.johnyapps.cheers.viewmodels.StatisticsViewModel

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

        return binding.root
    }
}