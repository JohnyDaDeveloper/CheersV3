package cz.johnyapps.cheers.dialogs;

import android.content.Context;
import android.graphics.Color;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatAutoCompleteTextView;
import androidx.appcompat.widget.AppCompatEditText;

import java.util.List;

import cz.johnyapps.cheers.R;
import cz.johnyapps.cheers.adapters.listadapters.ItemsWithIdAdapter;
import cz.johnyapps.cheers.database.tasks.BaseDatabaseTask;
import cz.johnyapps.cheers.database.tasks.GetBeveragesTask;
import cz.johnyapps.cheers.entities.CounterWithBeverage;
import cz.johnyapps.cheers.entities.beverage.Beverage;
import cz.johnyapps.cheers.entities.counter.Counter;
import cz.johnyapps.cheers.tools.Logger;
import cz.johnyapps.cheers.tools.NumberUtils;
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
    @Nullable
    private final Beverage previousBeverage;

    public NewCounterDialog(@NonNull Context context, @Nullable Beverage previousBeverage) {
        this.context = context;
        this.adapter = new ItemsWithIdAdapter<>(context, this::setBeverage);
        this.previousBeverage = previousBeverage;

        GetBeveragesTask task = new GetBeveragesTask(context);
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

    public void show(@NonNull OnCounterCreatedListener onCounterCreatedListener,
                     @NonNull OnBeverageCreatedListener onBeverageCreatedListener) {
        show(onCounterCreatedListener, onBeverageCreatedListener, null, -1, -1);
    }

    public void show(@NonNull OnCounterCreatedListener onCounterCreatedListener,
                     @NonNull OnBeverageCreatedListener onBeverageCreatedListener,
                     @Nullable String name,
                     float alcohol,
                     float volume) {
        CustomDialogBuilder builder = new CustomDialogBuilder(context);
        builder.setTitle(R.string.dialog_new_counter_title)
                .setCancelable(false)
                .setView(R.layout.dialog_new_counter)
                .setPositiveButton(R.string.create, (dialog, which) ->
                        createCounter((AlertDialog) dialog,
                                previousBeverage,
                                onCounterCreatedListener,
                                onBeverageCreatedListener))
                .setNeutralButton(R.string.cancel, (dialog, which) -> {});

        alertDialog = builder.create();
        alertDialog.show();

        AppCompatAutoCompleteTextView nameEditText = alertDialog.findViewById(R.id.nameEditText);
        assert nameEditText != null;
        nameEditText.setAdapter(adapter);

        if (name != null && !name.isEmpty()) {
            nameEditText.setText(name);
        }

        AppCompatEditText alcoholEditText = alertDialog.findViewById(R.id.alcoholEditText);
        alcoholEditText.setEnabled(selectedBeverage == null);

        if (alcohol > 0) {
            assert alcoholEditText != null;
            alcoholEditText.setText(String.valueOf(alcohol));
        }

        if (volume > 0) {
            AppCompatEditText volumeEditText = alertDialog.findViewById(R.id.volumeEditText);
            assert volumeEditText != null;
            volumeEditText.setText(String.valueOf(volume));
        }
    }

    private void setBeverage(@NonNull Beverage beverage) {
        this.selectedBeverage = beverage;

        if (alertDialog != null) {
            AppCompatAutoCompleteTextView nameEditText = alertDialog.findViewById(R.id.nameEditText);
            assert nameEditText != null;
            nameEditText.dismissDropDown();
            nameEditText.setText(beverage.getName());

            AppCompatEditText volumeEditText = alertDialog.findViewById(R.id.volumeEditText);
            assert volumeEditText != null;
            volumeEditText.requestFocus();

            AppCompatEditText alcoholEditText = alertDialog.findViewById(R.id.alcoholEditText);
            assert alcoholEditText != null;

            if (beverage.getAlcohol() > 0) {
                alcoholEditText.setText(String.valueOf(beverage.getAlcohol()));
            }

            alcoholEditText.setEnabled(false);
        }
    }

    private void createCounter(@NonNull AlertDialog alertDialog,
                               @Nullable Beverage previousBeverage,
                               @NonNull OnCounterCreatedListener onCounterCreatedListener,
                               @NonNull OnBeverageCreatedListener onBeverageCreatedListener) {
        AutoCompleteTextView nameEditText = alertDialog.findViewById(R.id.nameEditText);
        assert nameEditText != null;
        String name = nameEditText.getText() == null ? null : nameEditText.getText().toString();

        AppCompatEditText alcoholEditText = alertDialog.findViewById(R.id.alcoholEditText);
        assert alcoholEditText != null;
        String alcoholString = alcoholEditText.getText() == null ? null : alcoholEditText.getText().toString().replaceAll(",", ".");
        float alcohol = NumberUtils.toFloat(alcoholString, -1);

        AppCompatEditText volumeEditText = alertDialog.findViewById(R.id.volumeEditText);
        assert volumeEditText != null;
        String volumeString = volumeEditText.getText() == null ? null : volumeEditText.getText().toString();
        float volume = NumberUtils.toFloat(volumeString, 0);

        if (name == null || name.isEmpty() || volume <= 0) {
            show(onCounterCreatedListener, onBeverageCreatedListener, name, alcohol, volume);
            Toast.makeText(context, R.string.dialog_new_counter_incomplete_data, Toast.LENGTH_LONG).show();
        } else {
            Beverage beverage;

            if (selectedBeverage == null) {
                int color = previousBeverage == null ? ThemeUtils.getRandomColor() : previousBeverage.getColor();
                beverage = new Beverage(name, ThemeUtils.getNextColor(color), Color.BLACK, alcohol);
                onBeverageCreatedListener.onCreated(beverage);
            } else {
                beverage = selectedBeverage;
            }

            Counter counter = new Counter(beverage.getId(), volume);
            onCounterCreatedListener.onCreated(new CounterWithBeverage(counter, beverage));
        }
    }



    public interface OnCounterCreatedListener {
        void onCreated(@NonNull CounterWithBeverage counterWithBeverage);
    }

    public interface OnBeverageCreatedListener {
        void onCreated(@NonNull Beverage beverage);
    }
}
