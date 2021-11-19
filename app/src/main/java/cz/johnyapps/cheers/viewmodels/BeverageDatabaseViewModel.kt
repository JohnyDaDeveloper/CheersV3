package cz.johnyapps.cheers.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import cz.johnyapps.cheers.entities.beverage.Beverage
import cz.johnyapps.cheers.repositories.BeverageRepository

class BeverageDatabaseViewModel(application: Application) : AndroidViewModel(application) {
    val repository = BeverageRepository(application, viewModelScope)

    val beverages = repository.getAllBeverages()
    val selectedBeverage: MutableLiveData<Beverage> = MutableLiveData()
}