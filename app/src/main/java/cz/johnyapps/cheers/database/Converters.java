package cz.johnyapps.cheers.database;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.TypeConverter;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cz.johnyapps.cheers.entities.Tag;
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
        JSONArray jsonArray = new JSONArray();

        for (Tag tag : tags) {
            try {
                jsonArray.put(tag.toJson());
            } catch (JSONException e) {
                Logger.w(TAG, "tagsToString: Failed to convert Tag to JSON", e);
            }
        }

        return jsonArray.toString();
    }

    @TypeConverter
    @NonNull
    public static List<Tag> stringToTags(@NonNull String string) {
        List<Tag> tags = new ArrayList<>();

        try {
            JSONArray jsonArray = new JSONArray(string);

            for (int i = 0; i < jsonArray.length(); i++) {
                tags.add(Tag.fromJson(jsonArray.getJSONObject(i)));
            }
        } catch (JSONException e) {
            Logger.w(TAG, "stringToTags: Failed to parse string: %s", string);
        }

        return tags;
    }
}
