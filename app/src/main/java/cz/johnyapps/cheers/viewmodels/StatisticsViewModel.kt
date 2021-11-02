package cz.johnyapps.cheers.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import cz.johnyapps.cheers.entities.CounterWithBeverage
import cz.johnyapps.cheers.repositories.BeverageRepository
import cz.johnyapps.cheers.views.graphview.GraphValue
import cz.johnyapps.cheers.views.graphview.GraphValueSet

class StatisticsViewModel(application: Application): AndroidViewModel(application) {
    val counters: LiveData<List<CounterWithBeverage>>
    val selectedGraphValueSet: MutableLiveData<GraphValueSet?> = MutableLiveData()
    val selectedGraphValue: MutableLiveData<GraphValue?> = MutableLiveData()

    init {
        val repository = BeverageRepository(application, viewModelScope)
        counters = repository.getAllActiveCountersWithBeverages()
    }

    fun setSetAndValue(graphValueSet: GraphValueSet, graphValue: GraphValue) {
        selectedGraphValueSet.value = graphValueSet
        selectedGraphValue.value = graphValue
    }
}