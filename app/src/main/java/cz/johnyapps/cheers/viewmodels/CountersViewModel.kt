package cz.johnyapps.cheers.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import cz.johnyapps.cheers.entities.CounterWithBeverage
import cz.johnyapps.cheers.entities.beverage.Beverage
import cz.johnyapps.cheers.repositories.BeverageRepository

class CountersViewModel(application: Application): AndroidViewModel(application) {
    val repository: BeverageRepository = BeverageRepository(application, viewModelScope)

    val countersWithBeverages: LiveData<List<CounterWithBeverage>> = repository.getAllActiveCountersWithBeverages()
    val selectedCounters = MutableLiveData<Collection<CounterWithBeverage>>()
    val beverages: LiveData<List<Beverage>> = repository.getAllBeverages()
}