package cz.johnyapps.cheers.database.tasks;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import cz.johnyapps.cheers.entities.counter.Counter;

public class DeleteCounterTask extends BaseDatabaseTask<Counter, Void, Void> {
    public DeleteCounterTask(@NonNull Context context) {
        super(context);
    }

    @Nullable
    @Override
    protected Void doInBackground(@Nullable Counter counter) throws Exception {

        if (counter != null) {
            getDatabase().counterDao().delete(counter.getId());
        }

        return null;
    }
}
