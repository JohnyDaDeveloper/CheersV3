package cz.johnyapps.cheers.fragments

import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import cz.johnyapps.cheers.R
import cz.johnyapps.cheers.adapters.recycler.BeverageFragmentAdapter
import cz.johnyapps.cheers.database.tasks.BaseDatabaseTask
import cz.johnyapps.cheers.database.tasks.InsertCounterWithBeverageTask
import cz.johnyapps.cheers.databinding.FragmentBeverageCategoriesBinding
import cz.johnyapps.cheers.dialogs.NewCounterDialog
import cz.johnyapps.cheers.entities.CounterWithBeverage
import cz.johnyapps.cheers.entities.beverage.Beverage
import cz.johnyapps.cheers.viewmodels.BeverageCategoriesViewModel
import java.lang.Exception

class BeverageCategoriesFragment: Fragment(), BackOptionFragment {
    private lateinit var viewModel: BeverageCategoriesViewModel
    private lateinit var binding: FragmentBeverageCategoriesBinding
    private lateinit var adapter: BeverageFragmentAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.beverage_categories_menu, menu)
        menu.findItem(R.id.addCounterMenuItem).setOnMenuItemClickListener {
            addCounter()
            false
        }
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val provider = ViewModelProvider(this)
        viewModel = provider.get(BeverageCategoriesViewModel::class.java)
        adapter = BeverageFragmentAdapter(this)

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_beverage_categories, container, false)
        binding.beveragesViewPager.adapter = adapter

        return binding.root
    }

    private fun addCounter() {
        val beverages: List<Beverage>? = viewModel.beverages.value
        val previousBeverage = if (beverages == null || beverages.isEmpty()) null else beverages[beverages.size - 1]
        val context = binding.root.context

        val newCounterDialog = NewCounterDialog(context, previousBeverage)
        newCounterDialog.show({ counterWithBeverage: CounterWithBeverage ->
            val task = InsertCounterWithBeverageTask(context)
            task.setOnCompleteListener(object : BaseDatabaseTask.OnCompleteListener<CounterWithBeverage?> {
                override fun onSuccess(counterWithBeverage: CounterWithBeverage?) {
                    if (counterWithBeverage != null) {
                        val fragment = adapter.getFragment(binding.beveragesViewPager.currentItem)
                        if (fragment is BeverageCategoryFragment) {
                            fragment.selectCounter(counterWithBeverage)
                        }
                    }
                }

                override fun onFailure(e: Exception?) {}
                override fun onComplete() {}
            })
            task.execute(counterWithBeverage)
        }) {  }
    }

    override fun onBackPressed(): Boolean {
        val fragment = adapter.getFragment(binding.beveragesViewPager.currentItem)

        if (fragment is BackOptionFragment) {
            return fragment.onBackPressed()
        }

        return false
    }
}