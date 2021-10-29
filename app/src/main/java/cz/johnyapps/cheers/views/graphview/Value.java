package cz.johnyapps.cheers.views.graphview;

import androidx.annotation.NonNull;

public class Value {
    private final float x;
    private final float y;
    private final int posInList;
    @NonNull
    private final GraphValueSet graphValueSet;
    @NonNull
    private final GraphValue graphValue;

    public Value(float x,
                 float y,
                 int posInList,
                 @NonNull GraphValueSet graphValueSet,
                 @NonNull GraphValue graphValue) {
        this.x = x;
        this.y = y;
        this.posInList = posInList;
        this.graphValueSet = graphValueSet;
        this.graphValue = graphValue;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public int getPosInList() {
        return posInList;
    }

    @NonNull
    public GraphValueSet getGraphValueSet() {
        return graphValueSet;
    }

    @NonNull
    public GraphValue getGraphValue() {
        return graphValue;
    }
}
