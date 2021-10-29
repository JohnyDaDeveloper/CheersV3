package cz.johnyapps.cheers.database.tasks;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import cz.johnyapps.cheers.entities.beverage.Beverage;

public class UpdateBeveragesTask extends BaseDatabaseTask<List<Beverage>, Void, List<Beverage>> {
    public UpdateBeveragesTask(@NonNull Context context) {
        super(context);
    }

    @Nullable
    @Override
    protected List<Beverage> doInBackground(@Nullable List<Beverage> beverages) throws Exception {
        if (beverages != null) {
            for (Beverage beverage : beverages) {
                getDatabase().beverageDao().insert(beverage);
            }
        }

        return beverages;
    }
}
