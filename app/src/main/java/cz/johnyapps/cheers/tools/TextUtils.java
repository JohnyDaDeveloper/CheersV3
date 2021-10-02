package cz.johnyapps.cheers.tools;

import androidx.annotation.NonNull;

import java.text.DecimalFormat;
import java.text.Normalizer;

public class TextUtils {
    @NonNull
    public static String decimalToStringWithTwoDecimalDigits(float value) {
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        return decimalFormat.format(value);
    }

    @NonNull
    public static CharSequence removeDiacritics(@NonNull CharSequence charSequence) {
        return Normalizer.normalize(charSequence, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
    }
}
