package cz.johnyapps.cheers.fragments

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import cz.johnyapps.cheers.R
import cz.johnyapps.cheers.adapters.recycler.CountersAdapter
import cz.johnyapps.cheers.adapters.recycler.SelectableAdapter
import cz.johnyapps.cheers.database.tasks.DeleteCountersTask
import cz.johnyapps.cheers.database.tasks.InsertCounterWithBeverageTask
import cz.johnyapps.cheers.database.tasks.InsertCountersTask
import cz.johnyapps.cheers.databinding.FragmentCountersBinding
import cz.johnyapps.cheers.dialogs.NewCounterDialog
import cz.johnyapps.cheers.dialogs.customdialogbuilder.CustomDialogBuilder
import cz.johnyapps.cheers.entities.CounterWithBeverage
import cz.johnyapps.cheers.entities.beverage.Beverage
import cz.johnyapps.cheers.tools.ThemeUtils
import cz.johnyapps.cheers.tools.TimeUtils
import cz.johnyapps.cheers.viewmodels.CountersViewModel

class CountersFragment: Fragment(), BackOptionFragment {
    private lateinit var viewModel: CountersViewModel
    private lateinit var binding: FragmentCountersBinding
    private val adapter = CountersAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val provider = ViewModelProvider(this)
        viewModel = provider.get(CountersViewModel::class.java)
        adapter.onSelectListener = object : SelectableAdapter.OnSelectListener<CounterWithBeverage> {
            override fun onSelect(selectedItems: Collection<CounterWithBeverage>?) {
                viewModel.selectedCounters.value = selectedItems
            }
        }

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_counters, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        setupRecyclerView()
        setupObservers()

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        val selectedCounters: Collection<CounterWithBeverage>? = viewModel.selectedCounters.value
        if (selectedCounters == null || selectedCounters.isEmpty()) {
            inflater.inflate(R.menu.counters_menu, menu)
            menu.findItem(R.id.addCounterMenuItem).setOnMenuItemClickListener {
                addCounter()
                false
            }
        } else {
            inflater.inflate(R.menu.selected_counter_menu, menu)
            val selectAllMenuItem = menu.findItem(R.id.selectAllMenuItem)

            if (selectedCounters.size == adapter.itemCount) {
                selectAllMenuItem.icon.setTint(
                    ThemeUtils.getAttributeColor(
                        R.attr.colorSecondary,
                        requireContext()
                    )
                )
            } else {
                selectAllMenuItem.icon.setTint(
                    ThemeUtils.getAttributeColor(
                        R.attr.colorOnToolbar,
                        requireContext()
                    )
                )
            }

            selectAllMenuItem.setOnMenuItemClickListener {
                adapter.selectAllOrCancelSelection()
                false
            }

            menu.findItem(R.id.deleteCounterMenuItem)
                .setOnMenuItemClickListener {
                    deleteCounters(selectedCounters)
                    false
                }

            menu.findItem(R.id.stopCounterMenuItem).setOnMenuItemClickListener {
                stopCounters(selectedCounters)
                false
            }
        }
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun addCounter() {
        val beverages: List<Beverage>? = viewModel.beverages.value
        val previousBeverage = if (beverages == null || beverages.isEmpty()) null else beverages[beverages.size - 1]
        val context = binding.root.context

        val newCounterDialog = NewCounterDialog(context, previousBeverage)
        newCounterDialog.show({ counterWithBeverage: CounterWithBeverage ->
            val list = viewModel.countersWithBeverages.value

            if (list != null && list.isNotEmpty()) {
                binding.countersRecyclerView.scrollToPosition(list.size - 1)
            }

            val task = InsertCounterWithBeverageTask(context)
            task.execute(counterWithBeverage)
        }) { }
    }

    private fun deleteCounters(selectedCounters: Collection<CounterWithBeverage>) {
        val context: Context = binding.root.context
        val builder = CustomDialogBuilder(context)
        builder.setNeutralButton(R.string.cancel) { _, _ -> }
            .setPositiveButton(R.string.delete) { _, _ ->
                val iterator = selectedCounters.iterator()
                val counters = Array(selectedCounters.size) {
                    iterator.next().counter
                }

                val task = DeleteCountersTask(context)
                task.execute(counters)
            }

        if (selectedCounters.size == 1) {
            val counterWithBeverage: CounterWithBeverage = selectedCounters.iterator().next()
            builder.setTitle(
                context.resources.getString(
                    R.string.dialog_confirm_counter_delete_title,
                    counterWithBeverage.beverage.name
                )
            )
        } else {
            builder.setTitle(
                context.resources.getQuantityString(
                    R.plurals.dialog_confirm_counter_delete_title_multiple,
                    selectedCounters.size,
                    selectedCounters.size
                )
            )
        }

        builder.create().show()
    }

    private fun stopCounters(selectedCounters: Collection<CounterWithBeverage>) {
        val context: Context = binding.root.context
        val builder = CustomDialogBuilder(context)
        builder.setNeutralButton(R.string.cancel) { _, _ -> }
            .setPositiveButton(R.string.stop) { _, _ ->
                val iterator = selectedCounters.iterator()
                val counters = Array(selectedCounters.size) {
                    val counter = iterator.next().counter
                    counter.isActive = false
                    counter.endTime = TimeUtils.getDate()
                    counter
                }

                val task = InsertCountersTask(context)
                task.execute(counters)
            }

        if (selectedCounters.size == 1) {
            val counterWithBeverage: CounterWithBeverage = selectedCounters.iterator().next()
            builder.setTitle(
                context.resources.getString(
                    R.string.dialog_confirm_counter_stop_title,
                    counterWithBeverage.beverage.name
                )
            )
        } else {
            builder.setTitle(
                context.resources.getQuantityString(
                    R.plurals.dialog_confirm_counter_stop_title_multiple,
                    selectedCounters.size,
                    selectedCounters.size
                )
            )
        }

        builder.create().show()
    }

    private fun setupRecyclerView() {
        binding.countersRecyclerView.setAdapter(adapter, LinearLayoutManager(binding.root.context))
    }

    private fun setupObservers() {
        viewModel.countersWithBeverages.observe(viewLifecycleOwner, {
            adapter.submitList(it)
        })
        viewModel.selectedCounters.observe(viewLifecycleOwner, {
            activity?.invalidateOptionsMenu()
        })
    }

    override fun onBackPressed(): Boolean {
        if (adapter.isSelecting()) {
            adapter.cancelSelection()
            return true
        }

        return false
    }
}