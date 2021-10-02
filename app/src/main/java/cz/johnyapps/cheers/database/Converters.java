package cz.johnyapps.cheers.database;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cz.johnyapps.cheers.entities.Tag;
import cz.johnyapps.cheers.entities.counter.CounterEntry;
import cz.johnyapps.cheers.tools.Logger;

public class Converters {
    private static final String TAG = "Converters";

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

    @TypeConverter
    @NonNull
    public static String tagsToString(@NonNull List<Tag> tags) {
        return new Gson().toJson(tags);
    }

    @TypeConverter
    @NonNull
    public static List<Tag> stringToTags(@NonNull String string) {
        Type listType = new TypeToken<ArrayList<Tag>>(){}.getType();
        return new Gson().fromJson(string, listType);
    }

    @TypeConverter
    public static String counterEntriesToString(@NonNull List<CounterEntry> counterEntries) {
        JSONArray jsonArray = new JSONArray();

        try {
            for (CounterEntry counterEntry : counterEntries) {
                jsonArray.put(counterEntry.toJSON());
            }
        } catch (JSONException e) {
            Logger.w(TAG, "counterEntriesToString: Failed to CounterEntries to JSON");
        }

        return jsonArray.toString();
    }

    @TypeConverter
    public static List<CounterEntry> stringToCounterEntries(@NonNull String string) {
        List<CounterEntry> counterEntries = new ArrayList<>();

        try {
            JSONArray jsonArray = new JSONArray(string);

            for (int i = 0; i < jsonArray.length(); i++) {
                counterEntries.add(CounterEntry.fromJSON(jsonArray.getJSONObject(i)));
            }
        } catch (JSONException e) {
            Logger.w(TAG, "stringToCounterEntries: Failed to parse string %s", string);
        }

        return counterEntries;
    }
}
