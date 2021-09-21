package cz.johnyapps.cheers.database.tasks;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import cz.johnyapps.cheers.entities.beverage.Beverage;

public class GetBeverageNamesWithIdsTask extends BaseDatabaseTask<Void, Void, List<Beverage>> {
    public GetBeverageNamesWithIdsTask(@NonNull Context context) {
        super(context);
    }

    @Nullable
    @Override
    protected List<Beverage> doInBackground(@Nullable Void aVoid) throws Exception {
        return getDatabase().beverageDao().getBeverages();
    }
}
