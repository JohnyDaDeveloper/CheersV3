package cz.johnyapps.cheers.database.tasks;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import cz.johnyapps.cheers.entities.CounterWithBeverage;
import cz.johnyapps.cheers.entities.beverage.Beverage;
import cz.johnyapps.cheers.entities.counter.Counter;
import cz.johnyapps.cheers.tools.Logger;

public class InsertCounterWithBeverageTask extends BaseDatabaseTask<CounterWithBeverage, Void, CounterWithBeverage> {
    private static final String TAG = "InsertCounterDatabaseTask";

    public InsertCounterWithBeverageTask(@NonNull Context context) {
        super(context);
    }

    @Nullable
    @Override
    protected CounterWithBeverage doInBackground(@Nullable CounterWithBeverage counterWithBeverage) throws Exception {
        Counter counter = counterWithBeverage.getCounter();
        Beverage beverage = counterWithBeverage.getBeverage();

        String beverageMessage = "beverage is null";
        if (beverage != null) {
            beverageMessage = "beverage already in database";
            Beverage savedBeverage = getDatabase().beverageDao().get(beverage.getName());

            long beverageId;
            if (savedBeverage == null) {
                beverageId = getDatabase().beverageDao().insert(beverage);
                beverage.setId(beverageId);
                beverageMessage = String.format("beverage inserted with id %s", beverageId);
            } else {
                beverageId = savedBeverage.getId();
            }

            counter.setBeverageId(beverageId);
        }

        long counterId = getDatabase().counterDao().insert(counter);
        counter.setId(counterId);

        Logger.v(TAG, "doInBackground: Counter inserted with id %s, %s", counterId, beverageMessage);
        return counterWithBeverage;
    }
}
