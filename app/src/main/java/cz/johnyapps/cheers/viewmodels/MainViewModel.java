package cz.johnyapps.cheers.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

import cz.johnyapps.cheers.database.tasks.BaseDatabaseTask;
import cz.johnyapps.cheers.database.tasks.GetBeveragesTask;
import cz.johnyapps.cheers.database.tasks.GetCountersWithBeveragesTask;
import cz.johnyapps.cheers.entities.CounterWithBeverage;
import cz.johnyapps.cheers.entities.beverage.Beverage;
import cz.johnyapps.cheers.tools.Logger;

public class MainViewModel extends AndroidViewModel {
    private static final String TAG = "MainViewModel";

    @NonNull
    private final MutableLiveData<List<CounterWithBeverage>> countersWithBeverages = new MutableLiveData<>();
    @NonNull
    private final MutableLiveData<CounterWithBeverage> selectedCounterWithBeverage = new MutableLiveData<>();

    @NonNull
    private final MutableLiveData<List<Beverage>> beverages = new MutableLiveData<>();
    @NonNull
    private final MutableLiveData<Beverage> selectedBeverage = new MutableLiveData<>();

    public MainViewModel(@NonNull Application application) {
        super(application);
        fetchCounters();
        fetchBeverages();
    }

    private void fetchCounters() {
        GetCountersWithBeveragesTask task = new GetCountersWithBeveragesTask(getApplication());
        task.setOnCompleteListener(new BaseDatabaseTask.OnCompleteListener<List<CounterWithBeverage>>() {
            @Override
            public void onSuccess(@Nullable List<CounterWithBeverage> counterWithBeverages) {
                setCountersWithBeverages(counterWithBeverages);
            }

            @Override
            public void onFailure(@Nullable Exception e) {
                Logger.e(TAG, "fetchCounters: Failed to fetch counters", e);
            }

            @Override
            public void onComplete() {

            }
        });
        task.execute();
    }

    private void fetchBeverages() {
        GetBeveragesTask task = new GetBeveragesTask(getApplication());
        task.setOnCompleteListener(new BaseDatabaseTask.OnCompleteListener<List<Beverage>>() {
            @Override
            public void onSuccess(@Nullable List<Beverage> beverages) {
                Logger.d(TAG, "onSuccess: %s", beverages == null ? null : beverages.size());
                setBeverages(beverages);
            }

            @Override
            public void onFailure(@Nullable Exception e) {
                Logger.e(TAG, "onFailure: Failed to fetch beverages", e);
            }

            @Override
            public void onComplete() {

            }
        });
        task.execute();
    }

    public void updateCountersWithBeverage(@NonNull Beverage beverage) {
        List<CounterWithBeverage> countersWithBeverages = this.countersWithBeverages.getValue();
        boolean someAltered = false;

        if (countersWithBeverages != null) {
            for (CounterWithBeverage counterWithBeverage : countersWithBeverages) {
                if (counterWithBeverage.getBeverage().getId() == beverage.getId()) {
                    counterWithBeverage.setBeverage(beverage);
                    someAltered = true;
                }
            }
        }

        if (someAltered) {
            setCountersWithBeverages(countersWithBeverages);
        }
    }

    @NonNull
    public LiveData<List<CounterWithBeverage>> getCountersWithBeverages() {
        return countersWithBeverages;
    }

    public void setCountersWithBeverages(@Nullable List<CounterWithBeverage> countersWithBeverages) {
        this.countersWithBeverages.setValue(countersWithBeverages);
    }

    @NonNull
    public List<CounterWithBeverage> addCounterWithBeverage(@NonNull CounterWithBeverage counterWithBeverage) {
        List<CounterWithBeverage> countersWithBeverages = this.countersWithBeverages.getValue();

        if (countersWithBeverages == null) {
            countersWithBeverages = new ArrayList<>();
        }

        countersWithBeverages.add(counterWithBeverage);
        setCountersWithBeverages(countersWithBeverages);
        return countersWithBeverages;
    }

    public void removeCounterWithBeverage(@NonNull CounterWithBeverage counterWithBeverage) {
        List<CounterWithBeverage> countersWithBeverages = this.countersWithBeverages.getValue();

        if (countersWithBeverages != null) {
            countersWithBeverages.remove(counterWithBeverage);
        }

        setCountersWithBeverages(countersWithBeverages);
    }

    @NonNull
    public LiveData<CounterWithBeverage> getSelectedCounterWithBeverage() {
        return selectedCounterWithBeverage;
    }

    public void setSelectedCounterWithBeverage(@Nullable CounterWithBeverage selectedCounterWithBeverage) {
        this.selectedCounterWithBeverage.setValue(selectedCounterWithBeverage);
    }

    @NonNull
    public LiveData<List<Beverage>> getBeverages() {
        return beverages;
    }

    public void setBeverages(@Nullable List<Beverage> beverages) {
        this.beverages.setValue(beverages);
    }

    public void addBeverage(@NonNull Beverage beverage) {
        List<Beverage> beverages = this.beverages.getValue();

        if (beverages == null) {
            beverages = new ArrayList<>();
        }

        beverages.add(beverage);
        setBeverages(beverages);
    }

    @NonNull
    public LiveData<Beverage> getSelectedBeverage() {
        return selectedBeverage;
    }

    public void setSelectedBeverage(@Nullable Beverage selectedBeverage) {
        this.selectedBeverage.setValue(selectedBeverage);
    }
}
