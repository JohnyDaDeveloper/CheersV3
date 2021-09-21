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
import cz.johnyapps.cheers.database.tasks.GetCountersWithBeveragesTask;
import cz.johnyapps.cheers.entities.CounterWithBeverage;
import cz.johnyapps.cheers.tools.Logger;

public class MainViewModel extends AndroidViewModel {
    private static final String TAG = "MainViewModel";

    @NonNull
    private final MutableLiveData<List<CounterWithBeverage>> countersWithBeverages = new MutableLiveData<>();
    @NonNull
    private final MutableLiveData<CounterWithBeverage> selectedCounterWithBeverage = new MutableLiveData<>();

    public MainViewModel(@NonNull Application application) {
        super(application);
        fetchCounters();
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
            boolean removed = countersWithBeverages.remove(counterWithBeverage);
            Logger.d(TAG, "removeCounterWithBeverage: %s", removed);
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
}
