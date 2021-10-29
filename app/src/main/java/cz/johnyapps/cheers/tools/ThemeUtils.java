package cz.johnyapps.cheers.tools;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Color;

import androidx.annotation.AttrRes;
import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;

import java.util.Random;

public class ThemeUtils {
    private static final String TAG = "ThemeUtils";

    @ColorInt
    public static int getAttributeColor(@AttrRes int attr, @NonNull Context context) {
        int[] attrs = new int[]{attr};
        int color;
        TypedArray typedArray = context.obtainStyledAttributes(attrs);

        try {
            color = typedArray.getColor(0, Color.BLACK);
        } finally {
            typedArray.recycle();
        }

        return color;
    }

    @ColorInt
    public static int getNextColor(@ColorInt int color) {
        int red = Math.round(Color.red(color) * 1.3f % 125) + 130;
        int green = Math.round(Color.green(color) * 1.5f % 125) + 130;
        int blue = Math.round(Color.blue(color) * 1.7f % 125) + 130;

        return Color.rgb(red, green, blue);
    }

    @ColorInt
    public static int getRandomColor() {
        Random random = new Random();

        int[] order = new int[]{-1, -1, -1};
        int pos = random.nextInt(3);
        order[pos] = 2;
        pos = (pos + 1) % 3;
        int val = random.nextInt(2);
        order[pos] = val;
        pos = (pos + 1) % 3;
        val = (val + 1) % 2;
        order[pos] = val;

        int[] colors = new int[3];
        for (int i = 0; i < colors.length; i++) {
            colors[i] = random.nextInt(255);
        }

        int color = Color.rgb(colors[order[0]], colors[order[1]], colors[order[2]]);
        float lum = Color.luminance(color);

        while (lum < 0.5) {
            pos = random.nextInt(3);
            colors[pos] = Math.min((int) (colors[pos] * 1.3f), 255);
            color = Color.rgb(colors[order[0]], colors[order[1]], colors[order[2]]);
            lum = Color.luminance(color);
        }

        return color;
    }

    @ColorInt
    public static int addAlpha(int alpha, int color) {
        return Color.argb(alpha, Color.red(color), Color.green(color), Color.blue(color));
    }

    public static boolean isSystemInNightMode(@NonNull Context context) {
        return (context.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES;
    }

    public static int dpToPx(@NonNull Context context, int dp) {
        return Math.round(dp * context.getResources().getDisplayMetrics().density);
    }
}
