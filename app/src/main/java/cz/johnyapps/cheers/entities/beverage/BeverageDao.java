package cz.johnyapps.cheers.entities.beverage;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
    @Query("SELECT * FROM beverage_table WHERE name = :beverageName")
    Beverage get(@NonNull String beverageName);

    @Nullable
    @Query("SELECT * FROM beverage_table")
    List<Beverage> getBeverages();
}
