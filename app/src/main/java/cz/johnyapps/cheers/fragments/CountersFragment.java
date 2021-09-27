package cz.johnyapps.cheers.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collection;
import java.util.List;

import cz.johnyapps.cheers.R;
import cz.johnyapps.cheers.adapters.recycleradapters.CountersAdapter;
import cz.johnyapps.cheers.database.tasks.DeleteCountersTask;
import cz.johnyapps.cheers.database.tasks.InsertCountersTask;
import cz.johnyapps.cheers.database.tasks.InsertCounterWithBeverageTask;
import cz.johnyapps.cheers.dialogs.CustomDialogBuilder;
import cz.johnyapps.cheers.dialogs.NewCounterDialog;
import cz.johnyapps.cheers.entities.CounterWithBeverage;
import cz.johnyapps.cheers.entities.beverage.Beverage;
import cz.johnyapps.cheers.entities.counter.Counter;
import cz.johnyapps.cheers.tools.Logger;
import cz.johnyapps.cheers.tools.ThemeUtils;
import cz.johnyapps.cheers.tools.TimeUtils;
import cz.johnyapps.cheers.viewmodels.MainViewModel;

public class CountersFragment extends Fragment implements BackOptionFragment {
    private static final String TAG = "CountersFragment";

    private MainViewModel viewModel;
    @Nullable
    private CountersAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupViewModel();
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_counters, container, false);

        setupObservers();
        setupRecyclerView(root);

        return root;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        Collection<CounterWithBeverage> selectedItems = viewModel.getSelectedCountersWithBeverages().getValue();

        if (selectedItems == null || selectedItems.isEmpty()) {
            inflater.inflate(R.menu.counters_menu, menu);

            menu.findItem(R.id.addCounterMenuItem).setOnMenuItemClickListener(item -> {
                addCounter();
                return false;
            });
        } else {
            inflater.inflate(R.menu.selected_counter_menu, menu);

            MenuItem selectAllMenuItem = menu.findItem(R.id.selectAllMenuItem);

            if (adapter != null && selectedItems.size() == adapter.getItemCount()) {
                selectAllMenuItem.getIcon().setTint(ThemeUtils.getAttributeColor(R.attr.colorSecondary, requireContext()));
            } else {
                selectAllMenuItem.getIcon().setTint(ThemeUtils.getAttributeColor(R.attr.colorOnToolbar, requireContext()));
            }

            selectAllMenuItem.setOnMenuItemClickListener(item -> {
                if (adapter != null) {
                    adapter.selectAllOrCancelSelection();
                } else {
                    Logger.w(TAG, "onCreateOptionsMenu: Adapter is null");
                }

                return false;
            });

            menu.findItem(R.id.deleteCounterMenuItem).setOnMenuItemClickListener(item -> {
                deleteCounters(selectedItems);
                return false;
            });

            menu.findItem(R.id.stopCounterMenuItem).setOnMenuItemClickListener(item -> {
                stopCounters(selectedItems);
                return false;
            });
        }

        super.onCreateOptionsMenu(menu, inflater);
    }

    private void stopCounters(@NonNull Collection<CounterWithBeverage> countersWithBeverages) {
        View root = getView();

        if (root == null) {
            Logger.w(TAG, "stopCounter: root is null");
            return;
        }

        Context context = root.getContext();
        CustomDialogBuilder builder = new CustomDialogBuilder(context);
        builder.setNeutralButton(R.string.cancel, (dialog, which) -> {})
                .setPositiveButton(R.string.stop, (dialog, which) -> stopCounters(context,
                        countersWithBeverages));

        if (countersWithBeverages.size() == 1) {
            CounterWithBeverage counterWithBeverage = countersWithBeverages.iterator().next();
            builder.setTitle(context.getResources().getString(R.string.dialog_confirm_counter_stop_title,
                    counterWithBeverage.getBeverage().getName()));
        } else {
            builder.setTitle(context.getResources().getQuantityString(R.plurals.dialog_confirm_counter_stop_title_multiple,
                    countersWithBeverages.size(), countersWithBeverages.size()));
        }

        builder.create().show();
    }

    private void stopCounters(@NonNull Context context,
                             @NonNull Collection<CounterWithBeverage> countersWithBeverages) {
        Counter[] counters = new Counter[countersWithBeverages.size()];
        int i = 0;

        for (CounterWithBeverage counterWithBeverage : countersWithBeverages) {
            Counter counter = counterWithBeverage.getCounter();
            counter.setActive(false);
            counter.setEndTime(TimeUtils.getDate());
            counters[i++] = counter;
        }

        InsertCountersTask task = new InsertCountersTask(context);
        task.execute(counters);
        viewModel.removeCounterWithBeverage(countersWithBeverages);
    }

    private void deleteCounters(@NonNull Collection<CounterWithBeverage> countersWithBeverages) {
        View root = getView();

        if (root == null) {
            Logger.w(TAG, "deleteCounter: root is null");
            return;
        }

        Context context = root.getContext();
        CustomDialogBuilder builder = new CustomDialogBuilder(context);
        builder.setNeutralButton(R.string.cancel, (dialog, which) -> {})
                .setPositiveButton(R.string.delete, (dialog, which) -> deleteCounters(context,
                        countersWithBeverages));

        if (countersWithBeverages.size() == 1) {
            CounterWithBeverage counterWithBeverage = countersWithBeverages.iterator().next();
            builder.setTitle(context.getResources().getString(R.string.dialog_confirm_counter_delete_title,
                    counterWithBeverage.getBeverage().getName()));
        } else {
            builder.setTitle(context.getResources().getQuantityString(R.plurals.dialog_confirm_counter_delete_title_multiple,
                    countersWithBeverages.size(), countersWithBeverages.size()));
        }

        builder.create().show();
    }

    private void deleteCounters(@NonNull Context context,
                               @NonNull Collection<CounterWithBeverage> countersWithBeverages) {
        Counter[] counters = new Counter[countersWithBeverages.size()];

        DeleteCountersTask task = new DeleteCountersTask(context);
        task.execute(counters);
        viewModel.removeCounterWithBeverage(countersWithBeverages);
    }

    private void addCounter() {
        View root = getView();

        if (root == null) {
            Logger.w(TAG, "addCounter: root is null");
            return;
        }

        List<Beverage> beverages = viewModel.getBeverages().getValue();
        Beverage previousBeverage = beverages == null || beverages.isEmpty() ? null : beverages.get(beverages.size() - 1);

        NewCounterDialog newCounterDialog = new NewCounterDialog(root.getContext(), previousBeverage);
        newCounterDialog.show(counterWithBeverage -> {
            List<CounterWithBeverage> countersWithBeverages = viewModel.addCounterWithBeverage(counterWithBeverage);

            RecyclerView countersRecyclerView = root.findViewById(R.id.countersRecyclerView);
            countersRecyclerView.scrollToPosition(countersWithBeverages.size() - 1);

            saveCounter(root.getContext(), counterWithBeverage);
        }, viewModel::addBeverage);
    }

    private void saveCounter(@NonNull Context context, @NonNull CounterWithBeverage countersWithBeverages) {
        InsertCounterWithBeverageTask task = new InsertCounterWithBeverageTask(context);
        task.execute(countersWithBeverages);
    }

    private void setupRecyclerView(@NonNull View root) {
        if (adapter == null) {
            adapter = new CountersAdapter(root.getContext(), viewModel.getCountersWithBeverages().getValue());
            adapter.setOnSelectListener(selectedItems -> {
                adapter.setAllCountersDisabled(selectedItems != null && !selectedItems.isEmpty());
                viewModel.setSelectedCountersWithBeverages(selectedItems);
            });
        }

        RecyclerView countersRecyclerView = root.findViewById(R.id.countersRecyclerView);
        countersRecyclerView.setLayoutManager(new LinearLayoutManager(root.getContext()));
        countersRecyclerView.setAdapter(adapter);
    }

    private void setupViewModel() {
        ViewModelProvider provider = new ViewModelProvider(requireActivity());
        viewModel = provider.get(MainViewModel.class);
    }

    private void setupObservers() {
        viewModel.getCountersWithBeverages().observe(getViewLifecycleOwner(), counters -> {
            if (adapter != null) {
                adapter.update(counters);
            }
        });
    }

    @Override
    public boolean onBackPressed() {
        Collection<CounterWithBeverage> counterWithBeverages = viewModel.getSelectedCountersWithBeverages().getValue();
        if (counterWithBeverages != null && !counterWithBeverages.isEmpty() && adapter != null) {
            adapter.cancelSelection();
            return true;
        }

        return false;
    }
}
