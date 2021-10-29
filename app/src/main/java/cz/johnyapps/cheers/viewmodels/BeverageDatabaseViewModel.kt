package cz.johnyapps.cheers.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import cz.johnyapps.cheers.entities.beverage.Beverage
import cz.johnyapps.cheers.repositories.BeverageRepository

class BeverageDatabaseViewModel(application: Application) : AndroidViewModel(application) {
    val beverages: LiveData<List<Beverage>>

    init {
        val beverageRepository = BeverageRepository(application)
        beverages = beverageRepository.getAllBeverages()
    }
}