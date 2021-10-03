package cz.johnyapps.cheers.activities;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.Menu;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import cz.johnyapps.cheers.R;
import cz.johnyapps.cheers.dialogs.AboutAppDialog;
import cz.johnyapps.cheers.fragments.BackOptionFragment;
import cz.johnyapps.cheers.viewmodels.MainViewModel;

public class MainActivity extends NavigationActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupViewModel();
        setupMenuItems();
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

    private void setupMenuItems() {
        getNavigationView().getMenu().clear();
        getNavigationView().inflateMenu(R.menu.nav_menu);
        Menu navMenu = getNavigationView().getMenu();

        navMenu.findItem(R.id.aboutAppMenuItem).setOnMenuItemClickListener(item -> {
            AboutAppDialog aboutAppDialog = new AboutAppDialog(this);
            aboutAppDialog.show();
            return false;
        });
    }

    private void setupViewModel() {
        ViewModelProvider provider = new ViewModelProvider(this);
        MainViewModel viewModel = provider.get(MainViewModel.class);

        viewModel.getSelectedCountersWithBeverages().observe(this, counterWithBeverage -> invalidateOptionsMenu());
        viewModel.getSelectedBeverage().observe(this, beverage -> invalidateOptionsMenu());
    }
}