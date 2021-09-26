package cz.johnyapps.cheers.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import java.util.Random;

import cz.johnyapps.cheers.R;
import cz.johnyapps.cheers.entities.BeverageCategory;
import cz.johnyapps.cheers.tools.SharedPrefsUtils;
import cz.johnyapps.cheers.views.CounterView;

public class BeverageCategoryFragment extends Fragment {
    @NonNull
    private final BeverageCategory beverageCategory;
    @Nullable
    private SharedPreferences generalPrefs;

    public BeverageCategoryFragment(@NonNull BeverageCategory beverageCategory) {
        this.beverageCategory = beverageCategory;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_beverage_category, container, false);

        setupCounter(root);

        return root;
    }

    private void setupCounter(@NonNull View root) {
        int count = getGeneralPrefs().getInt(beverageCategory.getCountPrefName(), 0);

        CounterView counterView = root.findViewById(R.id.counterView);
        counterView.setCount(count);
        counterView.setTitleText(beverageCategory.getTitleResId());
        counterView.setOnValueChangeListener(value -> getGeneralPrefs().edit().putInt(beverageCategory.getCountPrefName(), value).apply());

        AppCompatImageView beverageCategoryImageView = root.findViewById(R.id.beverageCategoryImageView);
        beverageCategoryImageView.setImageDrawable(ContextCompat.getDrawable(root.getContext(), beverageCategory.getImageResId()));
        beverageCategoryImageView.setOnClickListener(v -> playSound(v.getContext()));
    }

    private void playSound(@NonNull Context context) {
        int[] sounds = beverageCategory.getSounds();

        if (sounds.length == 0) {
            return;
        }

        int soundToPlay;

        if (sounds.length > 1) {
            soundToPlay = sounds[new Random().nextInt(sounds.length)];
        } else {
            soundToPlay = sounds[0];
        }

        MediaPlayer player = MediaPlayer.create(context, soundToPlay);
        player.start();
    }

    @NonNull
    public SharedPreferences getGeneralPrefs() {
        if (generalPrefs == null) {
            generalPrefs = SharedPrefsUtils.getGeneralPrefs(getContext());
        }

        return generalPrefs;
    }
}
