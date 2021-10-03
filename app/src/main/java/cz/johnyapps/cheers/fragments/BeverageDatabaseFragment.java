package cz.johnyapps.cheers.fragments;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import cz.johnyapps.cheers.R;
import cz.johnyapps.cheers.adapters.recycleradapters.BeveragesAdapter;
import cz.johnyapps.cheers.database.tasks.InsertBeverageTask;
import cz.johnyapps.cheers.dialogs.EditBeverageDialog;
import cz.johnyapps.cheers.entities.beverage.Beverage;
import cz.johnyapps.cheers.tools.Logger;
import cz.johnyapps.cheers.tools.ThemeUtils;
import cz.johnyapps.cheers.viewmodels.MainViewModel;
import cz.johnyapps.cheers.views.EmptyMessageRecyclerView;

public class BeverageDatabaseFragment extends Fragment implements BackOptionFragment {
    private static final String TAG = "BeverageDatabaseFragment";

    private MainViewModel viewModel;
    @Nullable
    private BeveragesAdapter adapter;
    @Nullable
    private SearchView searchView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
        setupViewModel();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_beverage_database, container, false);
        setupRecyclerView(root);
        setupObservers();

        return root;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        Beverage selectedBeverage = viewModel.getSelectedBeverage().getValue();

        if (selectedBeverage == null) {
            inflater.inflate(R.menu.beverages_menu, menu);

            menu.findItem(R.id.addBeverageMenuItem).setOnMenuItemClickListener(item -> {
                addBeverage();
                return false;
            });

            setupSearch(menu);
        } else {
            inflater.inflate(R.menu.selected_beverage_menu, menu);

            menu.findItem(R.id.editBeverageMenuItem).setOnMenuItemClickListener(item -> {
                editSelectedBeverage();
                return false;
            });
        }

        super.onCreateOptionsMenu(menu, inflater);
    }

    private void setupSearch(@NonNull Menu menu) {
        MenuItem searchMenuItem = menu.findItem(R.id.searchMenuItem);

        if (adapter != null && adapter.isFiltered()) {
            searchView = null;

            MenuItem clearSearchMenuItem = menu.findItem(R.id.clearSearchMenuItem);
            clearSearchMenuItem.setVisible(true);
            clearSearchMenuItem.setOnMenuItemClickListener(item -> {
                adapter.clearFilter();
                Activity activity = getActivity();

                if (activity == null) {
                    Logger.w(TAG, "setupSearch: Activity is null");
                    return false;
                }

                activity.invalidateOptionsMenu();
                return false;
            });
            searchMenuItem.setVisible(false);
        } else {
            Activity activity = getActivity();
            if (activity != null) {
                SearchManager searchManager = (SearchManager) activity.getSystemService(Context.SEARCH_SERVICE);
                searchView = (SearchView) searchMenuItem.getActionView();
                searchView.setSearchableInfo(searchManager.getSearchableInfo(activity.getComponentName()));
                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        filterBeverages(newText);
                        return false;
                    }
                });

                SearchView.SearchAutoComplete searchAutoComplete = searchView.findViewById(R.id.search_src_text);
                searchAutoComplete.setTextColor(ThemeUtils.getAttributeColor(R.attr.colorOnToolbar, activity));
            } else {
                searchView = null;
                searchMenuItem.setVisible(false);
                Logger.w(TAG, "onCreateOptionsMenu: Activity is null");
            }
        }
    }

    private void filterBeverages(@NonNull String query) {
        if (adapter != null) {
            adapter.filter(query);
        } else {
            Logger.w(TAG, "filterBeverages: Adapter is null");
        }
    }

    private void addBeverage() {
        View root = getView();

        if (root == null) {
            Logger.w(TAG, "addBeverage: root is null");
            return;
        }

        Beverage beverage = new Beverage("", ThemeUtils.getRandomColor(), Color.BLACK, 0);
        EditBeverageDialog editBeverageDialog = new EditBeverageDialog(root.getContext());
        editBeverageDialog.show(beverage, beverage1 -> {
            InsertBeverageTask task = new InsertBeverageTask(root.getContext());
            task.execute(beverage1);
            
            viewModel.addBeverage(beverage1);
        });
    }

    private void editSelectedBeverage() {
        View root = getView();

        if (root == null) {
            Logger.w(TAG, "editSelectedBeverage: root is null");
            return;
        }

        Beverage beverage = viewModel.getSelectedBeverage().getValue();

        if (beverage != null) {
            EditBeverageDialog editBeverageDialog = new EditBeverageDialog(root.getContext());
            editBeverageDialog.show(beverage, beverage1 -> {
                if (adapter != null) {
                    InsertBeverageTask task = new InsertBeverageTask(root.getContext());
                    task.execute(beverage1);

                    adapter.cancelSelection();
                    viewModel.updateCountersWithBeverage(beverage1);
                }
            });
        } else {
            Logger.w(TAG, "editSelectedBeverage: Beverage is null");
        }
    }

    private void setupRecyclerView(@NonNull View root) {
        if (adapter == null) {
            adapter = new BeveragesAdapter(root.getContext(), viewModel.getBeverages().getValue());
            adapter.setOnSelectListener(selectedItems -> {
                if (selectedItems != null && !selectedItems.isEmpty()) {
                    viewModel.setSelectedBeverage(selectedItems.iterator().next());
                } else {
                    viewModel.setSelectedBeverage(null);
                }
            });
        }

        EmptyMessageRecyclerView beveragesRecyclerView = root.findViewById(R.id.beveragesRecyclerView);
        beveragesRecyclerView.setLayoutManager(new LinearLayoutManager(root.getContext()));
        beveragesRecyclerView.setAdapter(adapter);
    }

    private void setupViewModel() {
        ViewModelProvider provider = new ViewModelProvider(requireActivity());
        viewModel = provider.get(MainViewModel.class);
    }

    private void setupObservers() {
        viewModel.getBeverages().observe(getViewLifecycleOwner(), beverages -> {
            if (adapter != null) {
                adapter.update(beverages);
            }
        });
    }

    @Override
    public boolean onBackPressed() {
        if (searchView != null && !searchView.isIconified()) {
            searchView.setIconified(true);
            return true;
        }

        if (viewModel.getSelectedBeverage().getValue() != null && adapter != null) {
            adapter.cancelSelection();
            return true;
        }

        return false;
    }
}
