package cz.johnyapps.cheers.entities;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;

import cz.johnyapps.cheers.R;
import cz.johnyapps.cheers.SharedPrefsNames;

public enum BeverageCategory {
    BEER(R.string.beverage_beer, R.drawable.beer, SharedPrefsNames.BEER_SELECTED_COUNTER, R.raw.beer1, R.raw.beer2),
    WINE(R.string.beverage_wine, R.drawable.wine, SharedPrefsNames.WINE_SELECTED_COUNTER, R.raw.wine1, R.raw.wine2),
    SHOT(R.string.beverage_shot, R.drawable.shot, SharedPrefsNames.SHOT_SELECTED_COUNTER);

    private final int titleResId;
    private final int imageResId;
    @NonNull
    private final String selectedCounterPrefName;
    private final int[] sounds;

    BeverageCategory(@StringRes int titleResId,
                     @DrawableRes int imageResId,
                     @NonNull String selectedCounterPrefName,
                     int... sounds) {
        this.titleResId = titleResId;
        this.imageResId = imageResId;
        this.selectedCounterPrefName = selectedCounterPrefName;
        this.sounds = sounds;
    }

    public int getTitleResId() {
        return titleResId;
    }

    public int getImageResId() {
        return imageResId;
    }

    @NonNull
    public String getSelectedCounterPrefName() {
        return selectedCounterPrefName;
    }

    public int[] getSounds() {
        return sounds;
    }
}
