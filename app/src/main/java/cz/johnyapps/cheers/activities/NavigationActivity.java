package cz.johnyapps.cheers.activities;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;

import cz.johnyapps.cheers.R;

public abstract class NavigationActivity extends BaseActivity {
    private NavigationView navigationView;
    @Nullable
    private NavController.OnDestinationChangedListener onDestinationChangedListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());

        setupToolbar();
        setupNavigation();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return NavigationUI.onNavDestinationSelected(item, getNavController()) || super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(getNavController(), getAppBarConfiguration()) || super.onSupportNavigateUp();
    }

    @LayoutRes
    protected abstract int getLayoutId();

    public NavController getNavController() {
        return Navigation.findNavController(this, R.id.nav_host_fragment);
    }

    private AppBarConfiguration getAppBarConfiguration() {
        return new AppBarConfiguration.Builder(getNavController().getGraph())
                .setOpenableLayout(getDrawerLayout())
                .build();
    }

    public DrawerLayout getDrawerLayout() {
        return findViewById(R.id.main_drawer);
    }

    private void setupNavigation() {
        navigationView = findViewById(R.id.nav_view);
        NavController navController = getNavController();

        if (onDestinationChangedListener != null) {
            navController.addOnDestinationChangedListener(onDestinationChangedListener);
        }

        NavigationUI.setupWithNavController(navigationView, navController);
    }

    private void setupToolbar() {
        //Drawable drawable = ContextCompat.getDrawable(this, R.drawable.options_menu);

        Toolbar toolbar = findViewById(R.id.toolbar);
        //toolbar.setOverflowIcon(drawable);

        NavigationUI.setupWithNavController(toolbar, getNavController(), getAppBarConfiguration());
        setSupportActionBar(toolbar);
    }

    public NavigationView getNavigationView() {
        return navigationView;
    }

    public void setOnDestinationChangedListener(@Nullable NavController.OnDestinationChangedListener onDestinationChangedListener) {
        NavController navController = getNavController();

        if (onDestinationChangedListener != null) {
            navController.addOnDestinationChangedListener(onDestinationChangedListener);
        } else {
            navController.removeOnDestinationChangedListener(this.onDestinationChangedListener);
        }

        this.onDestinationChangedListener = onDestinationChangedListener;
    }

    @Nullable
    public Fragment getCurrentFragment() {
        Fragment navHostFragment = getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        return navHostFragment == null ? null : navHostFragment.getChildFragmentManager().getFragments().get(0);
    }
}
