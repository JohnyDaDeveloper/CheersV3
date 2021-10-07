package cz.johnyapps.cheers.tools;

import androidx.annotation.NonNull;

import java.text.SimpleDateFormat;
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

    @NonNull
    public static String toDateAndTime(@NonNull Date date) {
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm", getLocale());
        return format.format(date);
    }

    @NonNull
    public static String toTime(@NonNull Date date) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm", getLocale());
        return format.format(date);
    }
}
