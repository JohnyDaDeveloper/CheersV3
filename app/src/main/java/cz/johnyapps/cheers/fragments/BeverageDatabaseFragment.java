package cz.johnyapps.cheers.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
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
import cz.johnyapps.cheers.viewmodels.MainViewModel;

public class BeverageDatabaseFragment extends Fragment {
    private MainViewModel viewModel;
    @Nullable
    private BeveragesAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
