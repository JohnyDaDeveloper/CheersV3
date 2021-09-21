package cz.johnyapps.cheers.entities.counter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

import cz.johnyapps.cheers.entities.CounterWithBeverage;

@Dao
public interface CounterDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(@NonNull Counter counter);

    @Nullable
    @Transaction
    @Query("SELECT * FROM counter_table WHERE active = 1")
    List<CounterWithBeverage> getActiveCountersWithBeverages();

    @Query("UPDATE counter_table SET count = :count WHERE id = :id")
    void updateCount(long id, int count);

    @Query("DELETE FROM counter_table WHERE id = :counterId")
    void delete(long counterId);
}
