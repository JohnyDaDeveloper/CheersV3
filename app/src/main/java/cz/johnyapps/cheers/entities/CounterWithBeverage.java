package cz.johnyapps.cheers.entities;

import androidx.annotation.NonNull;
import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

import cz.johnyapps.cheers.entities.beverage.Beverage;
import cz.johnyapps.cheers.entities.counter.Counter;
import cz.johnyapps.cheers.views.graphview.GraphValue;
import cz.johnyapps.cheers.views.graphview.GraphValueSet;

public class CounterWithBeverage implements GraphValueSet {
    @Embedded
    @NonNull
    private Counter counter;

    @Relation(parentColumn = "beverageId", entityColumn = "id")
    @NonNull
    private Beverage beverage;

    public CounterWithBeverage(@NonNull Counter counter, @NonNull Beverage beverage) {
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

    @NonNull
    public Beverage getBeverage() {
        return beverage;
    }

    public void setBeverage(@NonNull Beverage beverage) {
        this.beverage = beverage;
    }

    @Override
    public int getColor() {
        return beverage.getColor();
    }

    @NonNull
    @Override
    public List<? extends GraphValue> getGraphValues() {
        return counter.getCounterEntries();
    }

    @Override
    public boolean sameValueForAllGraphValues() {
        return true;
    }
}
