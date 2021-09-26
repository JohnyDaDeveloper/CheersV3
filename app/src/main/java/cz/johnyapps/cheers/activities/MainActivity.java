package cz.johnyapps.cheers.activities;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import cz.johnyapps.cheers.R;
import cz.johnyapps.cheers.fragments.BackOptionFragment;
import cz.johnyapps.cheers.viewmodels.MainViewModel;

public class MainActivity extends NavigationActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupViewModel();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = getCurrentFragment();

        if (fragment instanceof BackOptionFragment && ((BackOptionFragment) fragment).onBackPressed()) {
            return;
        }

        super.onBackPressed();
    }

    private void setupViewModel() {
        ViewModelProvider provider = new ViewModelProvider(this);
        MainViewModel viewModel = provider.get(MainViewModel.class);

        viewModel.getSelectedCounterWithBeverage().observe(this, counterWithBeverage -> invalidateOptionsMenu());
        viewModel.getSelectedBeverage().observe(this, beverage -> invalidateOptionsMenu());
    }
}