package cz.johnyapps.cheers.repositories

import android.content.Context
import androidx.lifecycle.LiveData
import cz.johnyapps.cheers.database.CheersRoomDatabase
import cz.johnyapps.cheers.entities.CounterWithBeverage
import cz.johnyapps.cheers.entities.beverage.Beverage
import cz.johnyapps.cheers.entities.counter.Counter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BeverageRepository(context: Context, private val scope: CoroutineScope) {
    private val database = CheersRoomDatabase.getDatabase(context)

    fun getAllBeverages(): LiveData<List<Beverage>> {
        return database.beverageDao().liveDataBeverages
    }

    fun getAllActiveCountersWithBeverages(): LiveData<List<CounterWithBeverage>> {
        return database.counterDao().allActiveCounterWithBeveragesLiveData
    }

    fun updateCounter(counter: Counter) {
        scope.launch(Dispatchers.IO) {
            database.counterDao().updateCount(counter.id, counter.counterEntries)
        }
    }

    fun deleteCounters(counters: Array<Counter>) {
        scope.launch(Dispatchers.IO) {
            database.counterDao().delete(counters)
        }
    }

    fun insertBeverage(beverage: Beverage) {
        scope.launch(Dispatchers.IO) {
            beverage.id = database.beverageDao().insert(beverage)
        }
    }
}