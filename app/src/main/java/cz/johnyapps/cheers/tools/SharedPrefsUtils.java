package cz.johnyapps.cheers.tools;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import cz.johnyapps.cheers.R;

public class SharedPrefsUtils {
    @NonNull
    public static SharedPreferences getGeneralPrefs(@NonNull Context context) {
        return context.getSharedPreferences(String.format("%s_generalPrefs", context.getResources().getString(R.string.app_name)), Context.MODE_PRIVATE);
    }
}
