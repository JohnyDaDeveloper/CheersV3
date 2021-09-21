package cz.johnyapps.cheers.database.tasks;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import cz.johnyapps.cheers.entities.counter.Counter;

public class InsertCounterTask extends BaseDatabaseTask<Counter, Void, Counter> {
    public InsertCounterTask(@NonNull Context context) {
        super(context);
    }

    @Nullable
    @Override
    protected Counter doInBackground(@Nullable Counter counter) throws Exception {
        if (counter != null) {
            long id = getDatabase().counterDao().insert(counter);
            counter.setId(id);
        }

        return counter;
    }
}
