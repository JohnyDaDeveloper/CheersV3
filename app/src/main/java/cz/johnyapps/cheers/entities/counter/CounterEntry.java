package cz.johnyapps.cheers.entities.counter;

import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

import cz.johnyapps.cheers.entities.CounterWithBeverage;
import cz.johnyapps.cheers.tools.Logger;
import cz.johnyapps.cheers.tools.TimeUtils;
import cz.johnyapps.cheers.views.graphview.GraphValue;
import cz.johnyapps.cheers.views.graphview.GraphValueSet;

public class CounterEntry implements GraphValue {
    private static final String TAG = "CounterEntry";
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
    @Override
    public Date getTime() {
        return time;
    }

    @Override
    public float getValue(@NonNull GraphValueSet graphValueSet) {
        if (graphValueSet instanceof CounterWithBeverage) {
            CounterWithBeverage counterWithBeverage = (CounterWithBeverage) graphValueSet;
            float alcohol = counterWithBeverage.getBeverage().getAlcohol();

            if (alcohol > 0) {
                return counterWithBeverage.getCounter().getVolume() * alcohol;
            }
        } else {
            Logger.w(TAG, "getValue: Expected instance of %s, got %s",
                    CounterWithBeverage.class,
                    graphValueSet.getClass());
        }

        return 0;
    }
}
