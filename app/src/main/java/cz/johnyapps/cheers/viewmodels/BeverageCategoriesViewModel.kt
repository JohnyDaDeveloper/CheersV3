package cz.johnyapps.cheers.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData

import cz.johnyapps.cheers.entities.CounterWithBeverage
import cz.johnyapps.cheers.entities.beverage.Beverage
import cz.johnyapps.cheers.repositories.BeverageRepository

class BeverageCategoriesViewModel(application: Application): AndroidViewModel(application) {
    val countersWithBeverages: LiveData<List<CounterWithBeverage>>
    val beverages: LiveData<List<Beverage>>

    init {
        val repository = BeverageRepository(application)
        countersWithBeverages = repository.getAllActiveCountersWithBeverages()
        beverages = repository.getAllBeverages()
    }
}