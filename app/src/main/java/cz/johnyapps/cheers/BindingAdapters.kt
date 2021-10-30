package cz.johnyapps.cheers

import androidx.databinding.BindingAdapter
import cz.johnyapps.cheers.entities.CounterWithBeverage
import cz.johnyapps.cheers.views.CounterView

object BindingAdapters {
    @BindingAdapter("app:counterWithBeverage")
    @JvmStatic
    fun counterWithBeverage(counterView: CounterView, counterWithBeverage: CounterWithBeverage) {
        counterView.setCounter(counterWithBeverage)
    }
}