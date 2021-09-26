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

public class BeverageCategoriesFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_beverage_categories, container, false);

        setupViewPager(root);

        return root;
    }

    private void setupViewPager(@NonNull View root) {
        ViewPager2 beveragesViewPager = root.findViewById(R.id.beveragesViewPager);
        beveragesViewPager.setAdapter(new BeverageFragmentAdapter(this, BeverageCategory.values()));
    }
}
