package cz.johnyapps.cheers.activities;

import android.os.Bundle;

import androidx.lifecycle.ViewModelProvider;

import cz.johnyapps.cheers.R;
import cz.johnyapps.cheers.viewmodels.MainViewModel;

public class MainActivity extends NavigationActivity {
    private MainViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupViewModel();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    private void setupViewModel() {
        ViewModelProvider provider = new ViewModelProvider(this);
        viewModel = provider.get(MainViewModel.class);

        viewModel.getSelectedCounterWithBeverage().observe(this, counterWithBeverage -> invalidateOptionsMenu());
    }
}