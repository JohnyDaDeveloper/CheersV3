package cz.johnyapps.cheers.adapters.fragmentadapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;
import java.util.List;

import cz.johnyapps.cheers.entities.BeverageCategory;
import cz.johnyapps.cheers.fragments.BeverageCategoryFragment;

public class BeverageFragmentAdapter extends FragmentStateAdapter {
    @NonNull
    private final BeverageCategory[] beverageCategories;
    @NonNull
    private final List<Fragment> fragments = new ArrayList<>();

    public BeverageFragmentAdapter(@NonNull Fragment fragment,
                                   @NonNull BeverageCategory[] beverageCategories) {
        super(fragment);
        this.beverageCategories = beverageCategories;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment fragment = new BeverageCategoryFragment(beverageCategories[position]);
        fragments.add(fragment);
        return fragment;
    }

    @Override
    public int getItemCount() {
        return beverageCategories.length;
    }

    @Nullable
    public Fragment getFragment(int position) {
        if (fragments.size() > position) {
            return fragments.get(position);
        }

        return null;
    }
}
