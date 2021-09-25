package cz.johnyapps.cheers.observers;

public interface Observer<TYPE> {
    void update(TYPE type);
}
