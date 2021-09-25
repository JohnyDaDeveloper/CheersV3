package cz.johnyapps.cheers.tools;

import androidx.annotation.Nullable;

public class NumberUtils {
    public static float toFloat(@Nullable String stringValue, int defaultValue) {
        if (stringValue == null) {
            return defaultValue;
        }

        try {
            return Float.parseFloat(stringValue);
        } catch (Exception e) {
            return defaultValue;
        }
    }
}
