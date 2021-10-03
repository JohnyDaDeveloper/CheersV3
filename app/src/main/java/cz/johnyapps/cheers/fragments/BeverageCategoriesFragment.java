package cz.johnyapps.cheers.fragments;

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
import androidx.viewpager2.widget.ViewPager2;

import java.util.List;

import cz.johnyapps.cheers.R;
import cz.johnyapps.cheers.adapters.fragmentadapters.BeverageFragmentAdapter;
import cz.johnyapps.cheers.database.tasks.BaseDatabaseTask;
import cz.johnyapps.cheers.database.tasks.InsertCounterWithBeverageTask;
import cz.johnyapps.cheers.dialogs.NewCounterDialog;
import cz.johnyapps.cheers.entities.BeverageCategory;
import cz.johnyapps.cheers.entities.CounterWithBeverage;
import cz.johnyapps.cheers.entities.beverage.Beverage;
import cz.johnyapps.cheers.tools.Logger;
import cz.johnyapps.cheers.viewmodels.MainViewModel;
import cz.johnyapps.cheers.views.EmptyMessageRecyclerView;

public class BeverageCategoriesFragment extends Fragment implements BackOptionFragment {
    private static final String TAG = "BeverageCategoriesFragment";

    @Nullable
    private BeverageFragmentAdapter adapter = null;
    @Nullable
    private ViewPager2 beveragesViewPager = null;

    private MainViewModel viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupViewModel();
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_beverage_categories, container, false);

        setupViewPager(root);

        return root;
    }

    @Override
    public boolean onBackPressed() {
        if (adapter != null && beveragesViewPager != null) {
            Fragment fragment = adapter.getFragment(beveragesViewPager.getCurrentItem());

            if (fragment instanceof BackOptionFragment) {
                return ((BackOptionFragment) fragment).onBackPressed();
            }
        }

        return false;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.beverage_categories_menu, menu);

        menu.findItem(R.id.addCounterMenuItem).setOnMenuItemClickListener(item -> {
            addCounter();
            return false;
        });

        super.onCreateOptionsMenu(menu, inflater);
    }

    private void setupViewModel() {
        ViewModelProvider provider = new ViewModelProvider(requireActivity());
        viewModel = provider.get(MainViewModel.class);
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

            EmptyMessageRecyclerView countersRecyclerView = root.findViewById(R.id.countersRecyclerView);
            countersRecyclerView.scrollToPosition(countersWithBeverages.size() - 1);

            InsertCounterWithBeverageTask task = new InsertCounterWithBeverageTask(root.getContext());
            task.setOnCompleteListener(new BaseDatabaseTask.OnCompleteListener<CounterWithBeverage>() {
                @Override
                public void onSuccess(@Nullable CounterWithBeverage counterWithBeverage) {
                    if (beveragesViewPager != null && adapter != null) {
                        Fragment fragment = adapter.getFragment(beveragesViewPager.getCurrentItem());

                        if (fragment instanceof BeverageCategoryFragment) {
                            BeverageCategoryFragment beverageCategoryFragment = (BeverageCategoryFragment) fragment;
                            beverageCategoryFragment.selectLastCounter();
                        }
                    }
                }

                @Override
                public void onFailure(@Nullable Exception e) {

                }

                @Override
                public void onComplete() {

                }
            });
            task.execute(counterWithBeverage);
        }, viewModel::addBeverage);
    }

    private void setupViewPager(@NonNull View root) {
        adapter = new BeverageFragmentAdapter(this, BeverageCategory.values());
        beveragesViewPager = root.findViewById(R.id.beveragesViewPager);
        beveragesViewPager.setAdapter(adapter);
    }
}
