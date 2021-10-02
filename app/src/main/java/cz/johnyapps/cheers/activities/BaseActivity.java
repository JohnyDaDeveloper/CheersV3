package cz.johnyapps.cheers.activities;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.Map;

import cz.johnyapps.cheers.BuildConfig;
import cz.johnyapps.cheers.R;
import cz.johnyapps.cheers.SharedPrefsNames;
import cz.johnyapps.cheers.database.tasks.BaseDatabaseTask;
import cz.johnyapps.cheers.database.tasks.InsertCounterWithBeverageTask;
import cz.johnyapps.cheers.entities.CounterWithBeverage;
import cz.johnyapps.cheers.entities.beverage.Beverage;
import cz.johnyapps.cheers.entities.counter.Counter;
import cz.johnyapps.cheers.tools.Logger;
import cz.johnyapps.cheers.tools.SharedPrefsUtils;

public abstract class BaseActivity extends AppCompatActivity {
    private static final String TAG = "BaseActivity";

    @Nullable
    private SharedPreferences generaLPrefs = null;
    @NonNull
    private final Map<Integer, AppVersionMigration> migrationMap = new HashMap<>();
    private int completedMigrations = 0;
    private int atVersion = 0;
    @Nullable
    private OnMigrationFinishedListener onMigrationFinishedListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        createMigrations();
        super.onCreate(savedInstanceState);
    }

    private void createMigrations() {
        migrationMap.put(0, new AppVersionMigration(1) {
            @Override
            public void migrate() {
                final int[] taskCompleted = {0};

                Beverage beverage = new Beverage(getResources().getString(R.string.beverage_beer),
                        Color.rgb(252, 161, 3),
                        Color.BLACK,
                        0);
                Counter counter = new Counter(beverage.getId(), 0.5f);
                CounterWithBeverage counterWithBeverage = new CounterWithBeverage(counter, beverage);

                InsertCounterWithBeverageTask task1 = new InsertCounterWithBeverageTask(BaseActivity.this);
                task1.execute(counterWithBeverage);
                task1.setOnCompleteListener(new BaseDatabaseTask.OnCompleteListener<CounterWithBeverage>() {
                    @Override
                    public void onSuccess(@Nullable CounterWithBeverage counterWithBeverage) {
                        if (counterWithBeverage != null) {
                            getGeneraLPrefs().edit()
                                    .putLong(SharedPrefsNames.BEER_SELECTED_COUNTER,
                                            counterWithBeverage.getCounter().getId())
                                    .apply();
                        }
                    }

                    @Override
                    public void onFailure(@Nullable Exception e) {

                    }

                    @Override
                    public void onComplete() {
                        if (++taskCompleted[0] == 3) {
                            migrationCompleted(getToVersion());
                        }
                    }
                });

                beverage = new Beverage(getResources().getString(R.string.beverage_wine),
                        Color.rgb(219, 48, 48),
                        Color.BLACK,
                        0);
                counter = new Counter(beverage.getId(), 0.2f);
                counterWithBeverage = new CounterWithBeverage(counter, beverage);

                InsertCounterWithBeverageTask task2 = new InsertCounterWithBeverageTask(BaseActivity.this);
                task2.setOnCompleteListener(new BaseDatabaseTask.OnCompleteListener<CounterWithBeverage>() {
                    @Override
                    public void onSuccess(@Nullable CounterWithBeverage counterWithBeverage) {
                        if (counterWithBeverage != null) {
                            getGeneraLPrefs().edit()
                                    .putLong(SharedPrefsNames.WINE_SELECTED_COUNTER,
                                            counterWithBeverage.getCounter().getId())
                                    .apply();
                        }
                    }

                    @Override
                    public void onFailure(@Nullable Exception e) {

                    }

                    @Override
                    public void onComplete() {
                        if (++taskCompleted[0] == 3) {
                            migrationCompleted(getToVersion());
                        }
                    }
                });
                task2.execute(counterWithBeverage);

                beverage = new Beverage(getResources().getString(R.string.beverage_shot),
                        Color.rgb(184, 126, 50),
                        Color.BLACK,
                        0);
                counter = new Counter(beverage.getId(), 0.04f);
                counterWithBeverage = new CounterWithBeverage(counter, beverage);

                InsertCounterWithBeverageTask task3 = new InsertCounterWithBeverageTask(BaseActivity.this);
                task3.setOnCompleteListener(new BaseDatabaseTask.OnCompleteListener<CounterWithBeverage>() {
                    @Override
                    public void onSuccess(@Nullable CounterWithBeverage counterWithBeverage) {
                        if (counterWithBeverage != null) {
                            getGeneraLPrefs().edit()
                                    .putLong(SharedPrefsNames.SHOT_SELECTED_COUNTER,
                                            counterWithBeverage.getCounter().getId())
                                    .apply();
                        }
                    }

                    @Override
                    public void onFailure(@Nullable Exception e) {

                    }

                    @Override
                    public void onComplete() {
                        if (++taskCompleted[0] == 3) {
                            migrationCompleted(getToVersion());
                        }
                    }
                });
                task3.execute(counterWithBeverage);
            }
        });
    }

    protected void executeMigrations() {
        completedMigrations = 0;
        atVersion = getGeneraLPrefs().getInt(SharedPrefsNames.LAST_START_APP_VERSION, 0);

        if (atVersion == BuildConfig.VERSION_CODE) {
            if (onMigrationFinishedListener != null) {
                onMigrationFinishedListener.onMigrationsFinished();
            }
        } else {
            nextMigration();
        }
    }

    private void nextMigration() {
        if (atVersion < BuildConfig.VERSION_CODE) {
            MainActivity.AppVersionMigration migration = migrationMap.get(atVersion);

            if (migration != null) {
                Logger.i(TAG,
                        "checkIfActionNeeded: Executing migration from version %s to %s",
                        atVersion,
                        atVersion + 1);
                migration.migrate();
            } else {
                atVersion++;
                nextMigration();
            }
        }
    }

    private void migrationCompleted(int toVersion) {
        Logger.d(TAG, "migrationCompleted: Completed migration to version %s", toVersion);

        getGeneraLPrefs().edit()
                .putInt(SharedPrefsNames.LAST_START_APP_VERSION, toVersion)
                .apply();

        if (++completedMigrations == migrationMap.size() && onMigrationFinishedListener != null) {
            onMigrationFinishedListener.onMigrationsFinished();
        } else {
            atVersion++;
            nextMigration();
        }
    }

    @NonNull
    public SharedPreferences getGeneraLPrefs() {
        if (generaLPrefs == null) {
            generaLPrefs = SharedPrefsUtils.getGeneralPrefs(this);
        }

        return generaLPrefs;
    }

    public interface OnMigrationFinishedListener {
        void onMigrationsFinished();
    }

    public void setOnMigrationFinishedListener(@Nullable OnMigrationFinishedListener onMigrationFinishedListener) {
        this.onMigrationFinishedListener = onMigrationFinishedListener;
    }

    public static abstract class AppVersionMigration {
        private final int toVersion;

        public AppVersionMigration(int toVersion) {
            this.toVersion = toVersion;
        }

        public int getToVersion() {
            return toVersion;
        }

        public abstract void migrate();
    }
}
