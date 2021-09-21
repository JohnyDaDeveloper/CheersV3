package cz.johnyapps.cheers.entities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Embedded;
import androidx.room.Relation;

import cz.johnyapps.cheers.entities.beverage.Beverage;
import cz.johnyapps.cheers.entities.counter.Counter;

public class CounterWithBeverage {
    @Embedded
    @NonNull
    private Counter counter;

    @Relation(parentColumn = "beverageId", entityColumn = "id")
    @Nullable
    private Beverage beverage;

    public CounterWithBeverage(@NonNull Counter counter, @Nullable Beverage beverage) {
        this.counter = counter;
        this.beverage = beverage;
    }

    @NonNull
    public Counter getCounter() {
        return counter;
    }

    public void setCounter(@NonNull Counter counter) {
        this.counter = counter;
    }

    @Nullable
    public Beverage getBeverage() {
        return beverage;
    }

    public void setBeverage(@Nullable Beverage beverage) {
        this.beverage = beverage;
    }
}
