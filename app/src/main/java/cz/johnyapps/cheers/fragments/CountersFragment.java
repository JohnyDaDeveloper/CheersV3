package cz.johnyapps.cheers.fragments;

import android.content.Context;
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

import java.util.List;

import cz.johnyapps.cheers.R;
import cz.johnyapps.cheers.adapters.CountersAdapter;
import cz.johnyapps.cheers.database.tasks.DeleteCounterTask;
import cz.johnyapps.cheers.database.tasks.InsertCounterTask;
import cz.johnyapps.cheers.database.tasks.InsertCounterWithBeverageTask;
import cz.johnyapps.cheers.dialogs.CustomDialogBuilder;
import cz.johnyapps.cheers.dialogs.NewCounterDialog;
import cz.johnyapps.cheers.entities.CounterWithBeverage;
import cz.johnyapps.cheers.entities.beverage.Beverage;
import cz.johnyapps.cheers.entities.counter.Counter;
import cz.johnyapps.cheers.tools.Logger;
import cz.johnyapps.cheers.tools.TimeUtils;
import cz.johnyapps.cheers.viewmodels.MainViewModel;

public class CountersFragment extends Fragment {
    private static final String TAG = "CountersFragment";

    private MainViewModel viewModel;
    @Nullable
    private CountersAdapter countersAdapter;

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
        CounterWithBeverage selected = viewModel.getSelectedCounterWithBeverage().getValue();

        if (selected == null) {
            inflater.inflate(R.menu.counters_menu, menu);

            menu.findItem(R.id.addCounterMenuItem).setOnMenuItemClickListener(item -> {
                addCounter();
                return false;
            });
        } else {
            inflater.inflate(R.menu.selected_counter_menu, menu);

            menu.findItem(R.id.deleteCounterMenuItem).setOnMenuItemClickListener(item -> {
                deleteCounter(viewModel.getSelectedCounterWithBeverage().getValue());
                return false;
            });

            menu.findItem(R.id.stopCounterMenuItem).setOnMenuItemClickListener(item -> {
                stopCounter(viewModel.getSelectedCounterWithBeverage().getValue());
                return false;
            });
        }

        super.onCreateOptionsMenu(menu, inflater);
    }

    private void stopCounter(@Nullable CounterWithBeverage counterWithBeverage) {
        if (counterWithBeverage != null) {
            View root = getView();

            if (root == null) {
                Logger.w(TAG, "stopCounter: root is null");
                return;
            }

            Counter counter = counterWithBeverage.getCounter();
            Beverage beverage = counterWithBeverage.getBeverage();

            Context context = root.getContext();
            CustomDialogBuilder builder = new CustomDialogBuilder(context);
            builder.setTitle(context.getResources().getString(R.string.dialog_confirm_counter_stop_title, beverage.getName()))
                    .setPositiveButton(R.string.stop, (dialog, which) -> stopCounter(context,
                            counter,
                            counterWithBeverage))
                    .setNeutralButton(R.string.cancel, (dialog, which) -> {})
                    .create().show();
        } else {
            Logger.w(TAG, "stopCounter: None CounterWithBeverage selected");
        }
    }

    private void stopCounter(@NonNull Context context,
                             @NonNull Counter counter,
                             @NonNull CounterWithBeverage counterWithBeverage) {
        counter.setActive(false);
        counter.setEndTime(TimeUtils.getDate());
        InsertCounterTask task = new InsertCounterTask(context);
        task.execute(counter);
        viewModel.removeCounterWithBeverage(counterWithBeverage);
    }

    private void deleteCounter(@Nullable CounterWithBeverage counterWithBeverage) {
        if (counterWithBeverage != null) {
            View root = getView();

            if (root == null) {
                Logger.w(TAG, "deleteCounter: root is null");
                return;
            }
            Counter counter = counterWithBeverage.getCounter();
            Beverage beverage = counterWithBeverage.getBeverage();


            Context context = root.getContext();
            CustomDialogBuilder builder = new CustomDialogBuilder(context);
            builder.setTitle(context.getResources().getString(R.string.dialog_confirm_counter_delete_title, beverage.getName()))
                    .setPositiveButton(R.string.delete, (dialog, which) -> deleteCounter(context,
                            counter,
                            counterWithBeverage))
                    .setNeutralButton(R.string.cancel, (dialog, which) -> {})
                    .create().show();
        } else {
            Logger.w(TAG, "deleteCounter: None CounterWithBeverage selected");
        }
    }

    private void deleteCounter(@NonNull Context context,
                               @NonNull Counter counter,
                               @NonNull CounterWithBeverage counterWithBeverage) {
        DeleteCounterTask task = new DeleteCounterTask(context);
        task.execute(counter);
        viewModel.removeCounterWithBeverage(counterWithBeverage);
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
        if (countersAdapter == null) {
            countersAdapter = new CountersAdapter(root.getContext(), viewModel.getCountersWithBeverages().getValue());
            countersAdapter.setOnSelectListener(viewModel::setSelectedCounterWithBeverage);
        }

        RecyclerView countersRecyclerView = root.findViewById(R.id.countersRecyclerView);
        countersRecyclerView.setLayoutManager(new LinearLayoutManager(root.getContext()));
        countersRecyclerView.setAdapter(countersAdapter);
    }

    private void setupViewModel() {
        ViewModelProvider provider = new ViewModelProvider(requireActivity());
        viewModel = provider.get(MainViewModel.class);
    }

    private void setupObservers() {
        viewModel.getCountersWithBeverages().observe(getViewLifecycleOwner(), counters -> {
            if (countersAdapter != null) {
                countersAdapter.update(counters);
            }
        });
    }
}
