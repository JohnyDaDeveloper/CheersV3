package cz.johnyapps.cheers.tools;

import androidx.annotation.NonNull;

import java.text.DecimalFormat;

public class TextUtils {
    @NonNull
    public static String decimalToStringWithTwoDecimalDigits(float value) {
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        return decimalFormat.format(value);
    }
}
