package cz.johnyapps.cheers;

import android.content.Context;

import androidx.annotation.NonNull;

public interface ItemWithId {
    long getId();

    @NonNull
    String getText(@NonNull Context context);
}
