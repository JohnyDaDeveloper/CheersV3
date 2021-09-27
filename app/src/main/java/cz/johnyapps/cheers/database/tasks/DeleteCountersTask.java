package cz.johnyapps.cheers.database.tasks;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import cz.johnyapps.cheers.entities.counter.Counter;

public class DeleteCountersTask extends BaseDatabaseTask<Counter[], Void, Counter[]> {

    public DeleteCountersTask(@NonNull Context context) {
        super(context);
    }

    @Nullable
    @Override
    protected Counter[] doInBackground(@Nullable Counter[] counters) throws Exception {
        if (counters != null) {
            for (Counter counter : counters) {
                getDatabase().counterDao().delete(counter.getId());
            }
        }

        return counters;
    }
}
