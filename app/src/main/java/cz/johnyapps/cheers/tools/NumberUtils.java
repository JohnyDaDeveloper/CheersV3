package cz.johnyapps.cheers.tools;

import androidx.annotation.Nullable;

import java.math.BigDecimal;

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

    public static int roundUp(float f) {
        BigDecimal bigDecimal = new BigDecimal(f);
        bigDecimal = bigDecimal.setScale(0, BigDecimal.ROUND_UP);
        return bigDecimal.intValue();
    }

    public static int roundDown(float f) {
        BigDecimal bigDecimal = new BigDecimal(f);
        bigDecimal = bigDecimal.setScale(0, BigDecimal.ROUND_DOWN);
        return bigDecimal.intValue();
    }
}
