package cz.johnyapps.cheers.dialogs;

import android.content.Context;
import android.graphics.Color;
import android.widget.AutoCompleteTextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatAutoCompleteTextView;
import androidx.appcompat.widget.AppCompatEditText;

import java.util.List;

import cz.johnyapps.cheers.R;
import cz.johnyapps.cheers.adapters.listadapters.ItemsWithIdAdapter;
import cz.johnyapps.cheers.database.tasks.BaseDatabaseTask;
import cz.johnyapps.cheers.database.tasks.GetBeverageNamesWithIdsTask;
import cz.johnyapps.cheers.entities.CounterWithBeverage;
import cz.johnyapps.cheers.entities.beverage.Beverage;
import cz.johnyapps.cheers.entities.counter.Counter;
import cz.johnyapps.cheers.tools.Logger;
import cz.johnyapps.cheers.tools.ThemeUtils;

public class NewCounterDialog {
    private static final String TAG = "NewCounterDialog";

    @NonNull
    private final Context context;
    @NonNull
    private final ItemsWithIdAdapter<Beverage> adapter;
    @Nullable
    private AlertDialog alertDialog;
    @Nullable
    private Beverage selectedBeverage;

    public NewCounterDialog(@NonNull Context context) {
        this.context = context;
        this.adapter = new ItemsWithIdAdapter<>(context, this::setBeverage);

        GetBeverageNamesWithIdsTask task = new GetBeverageNamesWithIdsTask(context);
        task.setOnCompleteListener(new BaseDatabaseTask.OnCompleteListener<List<Beverage>>() {
            @Override
            public void onSuccess(@Nullable List<Beverage> beverages) {
                if (beverages != null) {
                    Logger.d(TAG, "%s beverage names retrieved", beverages.size());
                    adapter.update(beverages);
                }
            }

            @Override
            public void onFailure(@Nullable Exception e) {
                Logger.w(TAG, "Failed to retrieve beverage names", e);
            }

            @Override
            public void onComplete() {

            }
        });
        task.execute();
    }

    public void show(@NonNull OnCounterCreatedListener onCounterCreatedListener) {
        show(onCounterCreatedListener, null, -1, -1);
    }

    public void show(@NonNull OnCounterCreatedListener onCounterCreatedListener,
                     @Nullable String name,
                     float alcohol,
                     float volume) {
        CustomDialogBuilder builder = new CustomDialogBuilder(context);
        builder.setTitle(R.string.dialog_new_counter_title)
                .setCancelable(false)
                .setView(R.layout.dialog_new_counter)
                .setPositiveButton(R.string.create, (dialog, which) ->
                        createCounter((AlertDialog) dialog, onCounterCreatedListener))
                .setNeutralButton(R.string.cancel, (dialog, which) -> {});

        alertDialog = builder.create();
        alertDialog.show();

        AppCompatAutoCompleteTextView nameEditText = alertDialog.findViewById(R.id.nameEditText);
        nameEditText.setAdapter(adapter);

        if (name != null && !name.isEmpty()) {
            nameEditText.setText(name);
        }

        if (alcohol > -1) {
            AppCompatEditText alcoholEditText = alertDialog.findViewById(R.id.alcoholEditText);
            alcoholEditText.setText(String.valueOf(alcohol));
        }

        if (volume > -1) {
            AppCompatEditText volumeEditText = alertDialog.findViewById(R.id.volumeEditText);
            volumeEditText.setText(String.valueOf(volume));
        }
    }

    private void setBeverage(@NonNull Beverage beverage) {
        this.selectedBeverage = beverage;

        if (alertDialog != null) {
            AppCompatAutoCompleteTextView nameEditText = alertDialog.findViewById(R.id.nameEditText);
            nameEditText.dismissDropDown();
            nameEditText.setText(beverage.getName());

            AppCompatEditText volumeEditText = alertDialog.findViewById(R.id.volumeEditText);
            volumeEditText.requestFocus();

            AppCompatEditText alcoholEditText = alertDialog.findViewById(R.id.alcoholEditText);
            alcoholEditText.setText(String.valueOf(beverage.getAlcohol()));
            alcoholEditText.setEnabled(false);
        }
    }

    private void createCounter(@NonNull AlertDialog alertDialog,
                               @NonNull OnCounterCreatedListener onCounterCreatedListener) {
        AutoCompleteTextView nameEditText = alertDialog.findViewById(R.id.nameEditText);
        String name = nameEditText.getText() == null ? null : nameEditText.getText().toString();

        AppCompatEditText alcoholEditText = alertDialog.findViewById(R.id.alcoholEditText);
        String alcoholString = alcoholEditText.getText() == null ? null : alcoholEditText.getText().toString();
        float alcohol = toFloat(alcoholString, -1);

        AppCompatEditText volumeEditText = alertDialog.findViewById(R.id.volumeEditText);
        String volumeString = volumeEditText.getText() == null ? null : volumeEditText.getText().toString();
        float volume = toFloat(volumeString, -1);

        if (name == null || name.isEmpty() || volume < 0) {
            show(onCounterCreatedListener, name, alcohol, volume);
        } else {
            Beverage beverage;

            if (selectedBeverage == null) {
                beverage = new Beverage(name, ThemeUtils.getRandomColor(), Color.BLACK, alcohol);
            } else {
                beverage = selectedBeverage;
            }

            Counter counter = new Counter(beverage.getId(), volume);
            onCounterCreatedListener.onCreated(new CounterWithBeverage(counter, beverage));
        }
    }

    public float toFloat(@Nullable String stringValue, int defaultValue) {
        if (stringValue == null) {
            return defaultValue;
        }

        try {
            return Float.parseFloat(stringValue);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public interface OnCounterCreatedListener {
        void onCreated(@NonNull CounterWithBeverage counterWithBeverage);
    }
}
