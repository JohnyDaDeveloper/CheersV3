package cz.johnyapps.cheers.entities.beverage;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;
@Dao
public interface BeverageDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(@NonNull Beverage beverage);

    @Nullable
    @Query("SELECT * FROM beverage_table WHERE name = :name")
    Beverage get(@NonNull String name);

    @Nullable
    @Query("SELECT * FROM beverage_table WHERE deleted = 0")
    List<Beverage> getBeverages();

    @Query("SELECT * FROM beverage_table WHERE deleted = 0 ORDER BY LOWER(name) ASC")
    LiveData<List<Beverage>> getLiveDataBeverages();
}
