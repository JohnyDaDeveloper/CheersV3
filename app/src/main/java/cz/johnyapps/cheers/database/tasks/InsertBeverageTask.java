package cz.johnyapps.cheers.database.tasks;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import cz.johnyapps.cheers.entities.beverage.Beverage;

public class InsertBeverageTask extends BaseDatabaseTask<Beverage, Void, Beverage> {
    public InsertBeverageTask(@NonNull Context context) {
        super(context);
    }

    @Nullable
    @Override
    protected Beverage doInBackground(@Nullable Beverage beverage) throws Exception {
        if (beverage == null) {
            return null;
        }

        long id = getDatabase().beverageDao().insert(beverage);
        beverage.setId(id);

        return beverage;
    }
}
