package cz.johnyapps.cheers.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import cz.johnyapps.cheers.entities.beverage.Beverage;
import cz.johnyapps.cheers.entities.beverage.BeverageDao;
import cz.johnyapps.cheers.entities.counter.Counter;
import cz.johnyapps.cheers.entities.counter.CounterDao;

@Database(entities = {Beverage.class, Counter.class}, version = 1, exportSchema = false)
@TypeConverters(Converters.class)
public abstract class CheersRoomDatabase extends RoomDatabase {
    private static final String DATABASE_NAME = "cheers_database";

    public abstract BeverageDao beverageDao();
    public abstract CounterDao counterDao();

    @Nullable
    private static volatile CheersRoomDatabase instance;

    public static CheersRoomDatabase getDatabase(@NonNull Context context) {
        if (instance == null) {
            synchronized (CheersRoomDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(context, CheersRoomDatabase.class, DATABASE_NAME).build();
                }
            }
        }

        return instance;
    }
}
