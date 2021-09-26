package cz.johnyapps.cheers.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import cz.johnyapps.cheers.entities.BeverageCategory;
import cz.johnyapps.cheers.fragments.BeverageCategoryFragment;

public class BeverageFragmentAdapter extends FragmentStateAdapter {
    @NonNull
    private final BeverageCategory[] beverageCategories;

    public BeverageFragmentAdapter(@NonNull Fragment fragment,
                                   @NonNull BeverageCategory[] beverageCategories) {
        super(fragment);
        this.beverageCategories = beverageCategories;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return new BeverageCategoryFragment(beverageCategories[position]);
    }

    @Override
    public int getItemCount() {
        return beverageCategories.length;
    }
}
