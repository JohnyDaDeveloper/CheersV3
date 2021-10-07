package cz.johnyapps.cheers.views.graphview;

import androidx.annotation.NonNull;

import java.util.Date;

public interface GraphValue {
    @NonNull
    Date getTime();

    float getValue(@NonNull GraphValueSet graphValueSet);
}
