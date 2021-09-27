package cz.johnyapps.cheers.entities.counter;

import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

import cz.johnyapps.cheers.tools.TimeUtils;

public class CounterEntry {
    @NonNull
    private static final String TIME = "time";

    @NonNull
    private final Date time;

    public CounterEntry() {
        this.time = TimeUtils.getDate();
    }

    public CounterEntry(@NonNull Date time) {
        this.time = time;
    }

    //Cannot use Gson, converts time to String instead of long, worried about performance
    @NonNull
    public JSONObject toJSON() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(TIME, getTime().getTime());
        return jsonObject;
    }

    //Cannot use Gson, converts time to String instead of long, worried about performance
    @NonNull
    public static CounterEntry fromJSON(@NonNull JSONObject jsonObject) throws JSONException {
        Date time = new Date(jsonObject.getLong(TIME));
        return new CounterEntry(time);
    }

    @NonNull
    public Date getTime() {
        return time;
    }
}
