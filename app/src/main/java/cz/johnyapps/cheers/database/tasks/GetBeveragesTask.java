package cz.johnyapps.cheers.database.tasks;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Collections;
import java.util.List;

import cz.johnyapps.cheers.entities.beverage.Beverage;

public class GetBeveragesTask extends BaseDatabaseTask<Void, Void, List<Beverage>> {
    public GetBeveragesTask(@NonNull Context context) {
        super(context);
    }

    @Nullable
    @Override
    protected List<Beverage> doInBackground(@Nullable Void aVoid) throws Exception {
        List<Beverage> beverages = getDatabase().beverageDao().getBeverages();

        if (beverages != null) {
            Collections.sort(beverages);
        }

        return beverages;
    }
}
