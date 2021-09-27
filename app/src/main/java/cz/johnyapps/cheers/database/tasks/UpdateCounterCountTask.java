package cz.johnyapps.cheers.database.tasks;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.Gson;

import cz.johnyapps.cheers.database.Converters;
import cz.johnyapps.cheers.entities.counter.Counter;

public class UpdateCounterCountTask extends BaseDatabaseTask<Counter, Void, Counter> {
    public UpdateCounterCountTask(@NonNull Context context) {
        super(context);
    }

    @Nullable
    @Override
    protected Counter doInBackground(@Nullable Counter counter) throws Exception {
        if (counter != null) {
            getDatabase().counterDao().updateCount(counter.getId(), counter.getCounterEntries());
        }

        return counter;
    }
}
