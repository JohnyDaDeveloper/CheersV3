package cz.johnyapps.cheers.fragments

import android.app.Activity
import android.app.SearchManager
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.SearchView.SearchAutoComplete
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager

import cz.johnyapps.cheers.R
import cz.johnyapps.cheers.adapters.BeveragesAdapter
import cz.johnyapps.cheers.adapters.SelectableAdapter
import cz.johnyapps.cheers.database.tasks.InsertBeverageTask
import cz.johnyapps.cheers.database.tasks.UpdateBeveragesTask
import cz.johnyapps.cheers.databinding.FragmentBeverageDatabaseBinding
import cz.johnyapps.cheers.dialogs.EditBeverageDialog
import cz.johnyapps.cheers.dialogs.customdialogbuilder.CustomDialogBuilder
import cz.johnyapps.cheers.entities.beverage.Beverage
import cz.johnyapps.cheers.tools.Logger
import cz.johnyapps.cheers.tools.ThemeUtils
import cz.johnyapps.cheers.viewmodels.BeverageDatabaseViewModel

class BeverageDatabaseFragment: Fragment(), BackOptionFragment {
    private val adapter = BeveragesAdapter()
    private lateinit var binding: FragmentBeverageDatabaseBinding
    private lateinit var viewModel: BeverageDatabaseViewModel
    private var searchView: SearchView? = null

    companion object {
        private const val TAG = "BeverageDatabaseFragment"
    }

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
        viewModel = provider.get(BeverageDatabaseViewModel::class.java)

        adapter.setMultiSelection(false)
        adapter.onSelectListener = object : SelectableAdapter.OnSelectListener<Beverage> {
            override fun onSelect(selectedItems: Collection<Beverage>?) {
                if (selectedItems != null && !selectedItems.isEmpty()) {
                    viewModel.selectedBeverage.value = selectedItems.iterator().next()
                } else {
                    viewModel.selectedBeverage.value = null
                }
            }
        }

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_beverage_database, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        setupRecyclerView()
        setupObservers()

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        val selectedBeverage: Beverage? = viewModel.selectedBeverage.value

        if (selectedBeverage == null) {
            inflater.inflate(R.menu.beverages_menu, menu)
            menu.findItem(R.id.addBeverageMenuItem).setOnMenuItemClickListener {
                addBeverage()
                false
            }
            setupSearch(menu)
        } else {
            inflater.inflate(R.menu.selected_beverage_menu, menu)
            menu.findItem(R.id.editBeverageMenuItem).setOnMenuItemClickListener {
                editSelectedBeverage()
                false
            }
            menu.findItem(R.id.deleteBeverageMenuItem)
                .setOnMenuItemClickListener {
                    deleteSelectedBeverage()
                    false
                }
        }

        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun addBeverage() {
        val context = binding.root.context
        val beverage = Beverage("", ThemeUtils.getRandomColor(), Color.BLACK, 0f)
        val editBeverageDialog = EditBeverageDialog(context)
        editBeverageDialog.show(beverage) { beverage1: Beverage ->
            val task = InsertBeverageTask(context)
            task.execute(beverage1)
        }
    }

    private fun editSelectedBeverage() {
        val beverage: Beverage? = viewModel.selectedBeverage.value
        val context = binding.root.context

        if (beverage != null) {
            val editBeverageDialog = EditBeverageDialog(context)
            editBeverageDialog.show(beverage) { beverage1: Beverage ->
                val task = InsertBeverageTask(context)
                task.execute(beverage1)
                adapter.cancelSelection()
            }
        } else {
            Logger.w(TAG, "editSelectedBeverage: Beverage is null")
        }
    }

    private fun deleteSelectedBeverage() {
        val beverage: Beverage? = viewModel.selectedBeverage.value
        val context = binding.root.context

        if (beverage != null) {
            val builder = CustomDialogBuilder(context)
            builder.setTitle(
                context.resources.getString(R.string.dialog_confirm_beverage_delete_title, beverage.name)
            )
                .setPositiveButton(R.string.delete) { _, _ ->
                    beverage.isDeleted = true
                    val task = UpdateBeveragesTask(context)
                    task.execute(listOf(beverage))
                }
                .setNeutralButton(R.string.cancel) { _, _ -> }
                .create().show()
        } else {
            Logger.w(TAG, "deleteSelectedBeverage: Beverage is null")
        }
    }

    private fun setupSearch(menu: Menu) {
        val searchMenuItem = menu.findItem(R.id.searchMenuItem)

        if (adapter.isFiltered()) {
            searchView = null
            val clearSearchMenuItem = menu.findItem(R.id.clearSearchMenuItem)
            clearSearchMenuItem.isVisible = true
            clearSearchMenuItem.setOnMenuItemClickListener {
                adapter.clearFilter()
                activity?.invalidateOptionsMenu()
                false
            }
            searchMenuItem.isVisible = false
        } else {
            val activity: Activity? = activity

            if (activity != null) {
                val searchManager = activity.getSystemService(Context.SEARCH_SERVICE) as SearchManager
                val searchView = searchMenuItem.actionView as SearchView
                this.searchView = searchView
                searchView.setSearchableInfo(searchManager.getSearchableInfo(activity.componentName))
                searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String): Boolean {
                        return false
                    }

                    override fun onQueryTextChange(newText: String): Boolean {
                        adapter.filter(newText)
                        return false
                    }
                })

                val searchAutoComplete: SearchAutoComplete = searchView.findViewById(R.id.search_src_text)
                searchAutoComplete.setTextColor(
                    ThemeUtils.getAttributeColor(
                        R.attr.colorOnToolbar,
                        activity
                    )
                )
            } else {
                searchView = null
                searchMenuItem.isVisible = false
                Logger.w(TAG, "onCreateOptionsMenu: Activity is null")
            }
        }
    }

    private fun setupRecyclerView() {
        binding.beveragesRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.beveragesRecyclerView.adapter = adapter
    }

    private fun setupObservers() {
        viewModel.beverages.observe(viewLifecycleOwner, {
            adapter.submitList(it?.toMutableList())
        })
        viewModel.selectedBeverage.observe(viewLifecycleOwner, {
            activity?.invalidateOptionsMenu()
        })
    }

    override fun onBackPressed(): Boolean {
        return false
    }
}