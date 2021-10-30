package cz.johnyapps.cheers.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager

import cz.johnyapps.cheers.R
import cz.johnyapps.cheers.adapters.BeveragesAdapter
import cz.johnyapps.cheers.databinding.FragmentBeverageDatabaseNewBinding
import cz.johnyapps.cheers.viewmodels.BeverageDatabaseViewModel

class BeverageDatabaseFragmentNew: Fragment() {
    val adapter = BeveragesAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val provider = ViewModelProvider(this)
        val viewModel: BeverageDatabaseViewModel = provider.get(BeverageDatabaseViewModel::class.java)

        val binding: FragmentBeverageDatabaseNewBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_beverage_database_new, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        setupRecyclerView(binding)
        setupObservers(viewModel)

        return binding.root
    }

    private fun setupRecyclerView(binding: FragmentBeverageDatabaseNewBinding) {
        binding.beveragesRecyclerView.setLayoutManager(LinearLayoutManager(requireContext()))
        binding.beveragesRecyclerView.setAdapter(adapter)
    }

    private fun setupObservers(viewModel: BeverageDatabaseViewModel) {
        viewModel.beverages.observe(viewLifecycleOwner, {
            adapter.submitList(it?.toMutableList())
        })
    }
}