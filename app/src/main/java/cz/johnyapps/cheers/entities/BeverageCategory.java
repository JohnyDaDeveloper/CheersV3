package cz.johnyapps.cheers.entities;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;

import cz.johnyapps.cheers.R;
import cz.johnyapps.cheers.SharedPrefsNames;

public enum  BeverageCategory {
    BEER(R.string.beverage_beer, R.drawable.beer, SharedPrefsNames.BEER_COUNT),
    WINE(R.string.beverage_wine, R.drawable.wine, SharedPrefsNames.WINE_COUNT),
    SHOT(R.string.beverage_shot, R.drawable.shot, SharedPrefsNames.SHOT_COUNT);

    private final int titleResId;
    private final int imageResId;
    @NonNull
    private final String countPrefName;

    BeverageCategory(@StringRes int titleResId,
                     @DrawableRes int imageResId,
                     @NonNull String countPrefName) {
        this.titleResId = titleResId;
        this.imageResId = imageResId;
        this.countPrefName = countPrefName;
    }

    public int getTitleResId() {
        return titleResId;
    }

    public int getImageResId() {
        return imageResId;
    }

    @NonNull
    public String getCountPrefName() {
        return countPrefName;
    }
}
