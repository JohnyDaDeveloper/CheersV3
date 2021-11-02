package cz.johnyapps.cheers.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import cz.johnyapps.cheers.entities.beverage.Beverage
import cz.johnyapps.cheers.repositories.BeverageRepository

class BeverageDatabaseViewModel(application: Application) : AndroidViewModel(application) {
    val beverages: LiveData<List<Beverage>>
    val selectedBeverage: MutableLiveData<Beverage> = MutableLiveData()

    init {
        val beverageRepository = BeverageRepository(application, viewModelScope)
        beverages = beverageRepository.getAllBeverages()
    }
}