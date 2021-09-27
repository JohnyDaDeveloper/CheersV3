package cz.johnyapps.cheers.database.tasks;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import cz.johnyapps.cheers.entities.counter.Counter;

public class InsertCountersTask extends BaseDatabaseTask<Counter[], Void, Counter[]> {
    public InsertCountersTask(@NonNull Context context) {
        super(context);
    }

    @Nullable
    @Override
    protected Counter[] doInBackground(@Nullable Counter[] counters) throws Exception {
        if (counters != null) {
            for (Counter counter : counters) {
                long id = getDatabase().counterDao().insert(counter);
                counter.setId(id);
            }
        }

        return counters;
    }
}
