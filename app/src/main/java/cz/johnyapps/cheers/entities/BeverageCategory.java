package cz.johnyapps.cheers.entities;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;

import cz.johnyapps.cheers.R;
import cz.johnyapps.cheers.SharedPrefsNames;

public enum  BeverageCategory {
    BEER(R.string.beverage_beer, R.drawable.beer, SharedPrefsNames.BEER_COUNT, R.raw.beer1, R.raw.beer2),
    WINE(R.string.beverage_wine, R.drawable.wine, SharedPrefsNames.WINE_COUNT, R.raw.wine1),
    SHOT(R.string.beverage_shot, R.drawable.shot, SharedPrefsNames.SHOT_COUNT);

    private final int titleResId;
    private final int imageResId;
    @NonNull
    private final String countPrefName;
    private final int[] sounds;

    BeverageCategory(@StringRes int titleResId,
                     @DrawableRes int imageResId,
                     @NonNull String countPrefName,
                     int... sounds) {
        this.titleResId = titleResId;
        this.imageResId = imageResId;
        this.countPrefName = countPrefName;
        this.sounds = sounds;
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

    public int[] getSounds() {
        return sounds;
    }
}
