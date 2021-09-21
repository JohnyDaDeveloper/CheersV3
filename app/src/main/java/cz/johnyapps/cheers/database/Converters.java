package cz.johnyapps.cheers.database;

import androidx.annotation.Nullable;
import androidx.room.TypeConverter;

import java.util.Date;

public class Converters {
    @TypeConverter
    public static long dateToLong(@Nullable Date date) {
        if (date == null) {
            return -1;
        }

        return date.getTime();
    }

    @TypeConverter
    @Nullable
    public static Date longToDate(long time) {
        if (time == -1) {
            return null;
        }

        return new Date(time);
    }
}
