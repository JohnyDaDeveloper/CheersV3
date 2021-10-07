package cz.johnyapps.cheers.views.graphview;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;

import java.util.List;

public interface GraphValueSet {
    @ColorInt
    int getColor();

    @NonNull
    List<? extends GraphValue> getGraphValues();

    boolean sameValueForAllGraphValues();
}
