package cz.johnyapps.cheers.database.tasks;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.johnyapps.cheers.entities.beverage.Beverage;
import cz.johnyapps.cheers.entities.counter.Counter;
import cz.johnyapps.cheers.entities.counter.CounterEntry;

public class GetBeverageDescriptionTask extends BaseDatabaseTask<Beverage, Void, GetBeverageDescriptionTask.Result> {

    public GetBeverageDescriptionTask(@NonNull Context context) {
        super(context);
    }

    @Nullable
    @Override
    protected Result doInBackground(@Nullable Beverage beverage) throws Exception {
        if (beverage != null) {
            List<Counter> counters = getDatabase().counterDao().getCountersForBeverage(beverage.getId());

            if (counters != null && !counters.isEmpty()) {
                double total = 0;
                double lastVisit = -1;
                float lastVisitVolume = -1;
                Date lastVisitAt = null;
                Map<Double, Long> volumeMap = new HashMap<>();

                for (Counter counter : counters) {
                    List<CounterEntry> counterEntries = counter.getCounterEntries();
                    int entriesCount = counterEntries.size();
                    total += entriesCount * counter.getVolume();

                    if (lastVisitAt == null || counter.getStartTime().after(lastVisitAt)) {
                        lastVisitAt = counter.getStartTime();
                        lastVisit = entriesCount;
                        lastVisitVolume = counter.getVolume();
                    }

                    Double key = (double) counter.getVolume();
                    Long volumeCount = volumeMap.get(key);

                    if (volumeCount == null) {
                        volumeCount = (long) 0;
                    }

                    volumeMap.put(key, volumeCount + entriesCount);
                }

                return new Result(total, lastVisit, lastVisitVolume, volumeMap);
            }
        }

        return null;
    }

    public static class Result {
        private final double total;
        private final double lastVisit;
        private final float lastVisitVolume;
        @NonNull
        private final Map<Double, Long> volumeMap;

        public Result(double total,
                      double lastVisit,
                      float lastVisitVolume,
                      @NonNull Map<Double, Long> volumeMap) {
            this.total = total;
            this.lastVisit = lastVisit;
            this.lastVisitVolume = lastVisitVolume;
            this.volumeMap = volumeMap;
        }

        public double getTotal() {
            return total;
        }

        public double getLastVisit() {
            return lastVisit;
        }

        public float getLastVisitVolume() {
            return lastVisitVolume;
        }

        @NonNull
        public Map<Double, Long> getVolumeMap() {
            return volumeMap;
        }
    }
}
