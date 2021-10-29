package cz.johnyapps.cheers.repositories

import android.content.Context
import androidx.lifecycle.LiveData
import cz.johnyapps.cheers.database.CheersRoomDatabase
import cz.johnyapps.cheers.entities.beverage.Beverage

class BeverageRepository(context: Context) {
    private val database = CheersRoomDatabase.getDatabase(context)

    fun getAllBeverages(): LiveData<List<Beverage>> {
        return database.beverageDao().liveDataBeverages
    }
}