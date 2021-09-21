package cz.johnyapps.cheers.tools;

import androidx.annotation.NonNull;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class TimeUtils {
    @NonNull
    public static Locale getLocale() {
        return Locale.getDefault();
    }

    @NonNull
    public static TimeZone getTimeZone() {
        return TimeZone.getDefault();
    }

    @NonNull
    public static Date getDate() {
        Calendar calendar = Calendar.getInstance(getTimeZone(), getLocale());
        return calendar.getTime();
    }
}
