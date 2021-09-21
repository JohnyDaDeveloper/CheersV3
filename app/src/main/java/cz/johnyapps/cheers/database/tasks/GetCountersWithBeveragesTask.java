package cz.johnyapps.cheers.database.tasks;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import cz.johnyapps.cheers.entities.CounterWithBeverage;

public class GetCountersWithBeveragesTask extends BaseDatabaseTask<Void, Void, List<CounterWithBeverage>> {
    public GetCountersWithBeveragesTask(@NonNull Context context) {
        super(context);
    }

    @Nullable
    @Override
    protected List<CounterWithBeverage> doInBackground(@Nullable Void aVoid) throws Exception {
        return getDatabase().counterDao().getActiveCountersWithBeverages();
    }
}
