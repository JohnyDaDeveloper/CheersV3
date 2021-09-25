package cz.johnyapps.cheers.observers;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class Observable<TYPE> {
    private TYPE value;
    private final List<Observer<TYPE>> observers = new ArrayList<>();

    public Observable() {

    }

    public Observable(@Nullable TYPE baseValue) {
        super();
        this.value = baseValue;
    }

    public void setValue(@Nullable TYPE value) {
        this.value = value;
        notifyObservers();
    }

    @Nullable
    public TYPE getValue() {
        return value;
    }

    public void observe(@NonNull Observer<TYPE> observer) {
        observers.add(observer);
    }

    public void removeObserver(@NonNull Observer<TYPE> observer) {
        observers.remove(observer);
    }

    public void notifyObservers() {
        for (Observer<TYPE> observer : observers) {
            observer.update(getValue());
        }
    }
}
