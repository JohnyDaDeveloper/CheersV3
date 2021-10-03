package cz.johnyapps.cheers.tools;

import android.os.Build;
import android.text.Html;
import android.text.Spanned;

import androidx.annotation.NonNull;

import java.text.DecimalFormat;
import java.text.Normalizer;

public class TextUtils {
    @NonNull
    public static String decimalToStringWithTwoDecimalDigits(double value) {
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        return decimalFormat.format(value);
    }

    @NonNull
    public static CharSequence removeDiacritics(@NonNull CharSequence charSequence) {
        return Normalizer.normalize(charSequence, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
    }

    @NonNull
    public static Spanned fromHtml(@NonNull String html) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY);
        } else {
            //noinspection deprecation
            return Html.fromHtml(html);
        }
    }
}
