package cz.johnyapps.cheers.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import cz.johnyapps.cheers.R;
import cz.johnyapps.cheers.adapters.BeverageFragmentAdapter;
import cz.johnyapps.cheers.entities.BeverageCategory;

public class BeverageCategoriesFragment extends Fragment implements BackOptionFragment {
    @Nullable
    BeverageFragmentAdapter adapter = null;
    @Nullable
    ViewPager2 beveragesViewPager = null;

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

    private void setupViewPager(@NonNull View root) {
        adapter = new BeverageFragmentAdapter(this, BeverageCategory.values());
        beveragesViewPager = root.findViewById(R.id.beveragesViewPager);
        beveragesViewPager.setAdapter(adapter);
    }
}
