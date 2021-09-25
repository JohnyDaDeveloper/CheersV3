package cz.johnyapps.cheers.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import cz.johnyapps.cheers.R;
import cz.johnyapps.cheers.adapters.BeveragesAdapter;
import cz.johnyapps.cheers.database.tasks.InsertBeverageTask;
import cz.johnyapps.cheers.dialogs.EditBeverageDialog;
import cz.johnyapps.cheers.entities.beverage.Beverage;
import cz.johnyapps.cheers.tools.Logger;
import cz.johnyapps.cheers.tools.ThemeUtils;
import cz.johnyapps.cheers.viewmodels.MainViewModel;

public class BeverageDatabaseFragment extends Fragment {
    private static final String TAG = "BeverageDatabaseFragment";

    private MainViewModel viewModel;
    @Nullable
    private BeveragesAdapter adapter;

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
        } else {
            inflater.inflate(R.menu.selected_beverage_menu, menu);

            menu.findItem(R.id.editBeverageMenuItem).setOnMenuItemClickListener(item -> {
                editSelectedBeverage();
                return false;
            });
        }

        super.onCreateOptionsMenu(menu, inflater);
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

                    adapter.selectPosition(-1);
                    viewModel.updateCountersWithBeverage(beverage1);
                }
            });
        } else {
            Logger.w(TAG, "editSelectedBeverage: Beverage is null");
        }
    }

    private void setupRecyclerView(@NonNull View root) {
        if (adapter == null) {
            adapter = new BeveragesAdapter(root.getContext(),
                    viewModel.getBeverages().getValue(),
                    viewModel::setSelectedBeverage);
        }

        RecyclerView beveragesRecyclerView = root.findViewById(R.id.beveragesRecyclerView);
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
}
