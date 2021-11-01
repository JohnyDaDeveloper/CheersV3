package cz.johnyapps.cheers.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import cz.johnyapps.cheers.SharedPrefsNames;
import cz.johnyapps.cheers.tools.SharedPrefsUtils;
import cz.johnyapps.cheers.views.CounterView;

public class SplashActivity extends BaseActivity {
    private boolean migrationsFinished = false;
    private boolean counterViewMeasured = false;

    //TODO Remove
    private int counterHeight = 0;

    @NonNull
    public static final String COUNTER_HEIGHT = "counter_height";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setOnMigrationFinishedListener(() -> {
            if (counterViewMeasured) {
                startNextAndExit();
            }

            migrationsFinished = true;
        });

        executeMigrations();
        measureCounter();
    }

    private void measureCounter() {
        LinearLayout layout = new LinearLayout(this);
        CounterView counterView = new CounterView(this);
        counterView.setOnSizeChangedListener((width, height) -> {
            counterHeight = height;

            if (height > 0) {
                SharedPrefsUtils.getGeneralPrefs(this)
                        .edit()
                        .putInt(SharedPrefsNames.COUNTER_HEIGHT, height)
                        .apply();
            }

            if (migrationsFinished && !counterViewMeasured) {
                startNextAndExit();
            }

            counterViewMeasured = true;
        });
        counterView.setVisibility(View.INVISIBLE);

        layout.addView(counterView);
        setContentView(layout);
    }

    private void startNextAndExit() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(COUNTER_HEIGHT, counterHeight);
        startActivity(intent);
        finish();
    }
}
