package cz.johnyapps.cheers.views.graphview;

import androidx.annotation.NonNull;

public interface OnValueClickListener {
    void onValueClick(@NonNull GraphValueSet graphValueSet, @NonNull GraphValue graphValue);
}
