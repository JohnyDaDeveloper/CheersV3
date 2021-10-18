package cz.johnyapps.cheers.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import cz.johnyapps.cheers.R;
import cz.johnyapps.cheers.tools.Logger;
import cz.johnyapps.cheers.viewmodels.MainViewModel;
import cz.johnyapps.cheers.views.graphview.GraphView;

public class StatisticsFragment extends Fragment {
    private static final String TAG = "StatisticsFragment";

    private MainViewModel viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupViewModel();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_statistics, container, false);

        setupStatistics(root);
        setupObservers();

        return root;
    }

    private void setupObservers() {
        viewModel.getCountersWithBeverages().observe(getViewLifecycleOwner(), counterWithBeverages -> {
            View root = getView();

            if (root == null) {
                Logger.w(TAG, "setupObservers: root is null");
                return;
            }

            GraphView graphView = root.findViewById(R.id.graphView);
            graphView.setGraphValueSets(viewModel.getCountersWithBeverages().getValue());
        });
    }

    private void setupStatistics(@NonNull View root) {
        GraphView graphView = root.findViewById(R.id.graphView);
        graphView.setGraphValueSets(viewModel.getCountersWithBeverages().getValue());
    }

    private void setupViewModel() {
        ViewModelProvider provider = new ViewModelProvider(requireActivity());
        viewModel = provider.get(MainViewModel.class);
    }
}
