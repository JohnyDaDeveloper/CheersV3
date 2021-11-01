package cz.johnyapps.cheers

import android.util.Log
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.databinding.BindingAdapter
import cz.johnyapps.cheers.entities.CounterWithBeverage
import cz.johnyapps.cheers.entities.counter.CounterEntry
import cz.johnyapps.cheers.tools.TextUtils
import cz.johnyapps.cheers.tools.TimeUtils
import cz.johnyapps.cheers.views.CounterView
import cz.johnyapps.cheers.views.graphview.GraphValue
import cz.johnyapps.cheers.views.graphview.GraphValueSet
import cz.johnyapps.cheers.views.graphview.GraphView
import cz.johnyapps.cheers.views.graphview.OnValueClickListener

object BindingAdapters {
    @BindingAdapter("app:counterWithBeverage")
    @JvmStatic
    fun counterWithBeverage(counterView: CounterView, counterWithBeverage: CounterWithBeverage) {
        counterView.setCounter(counterWithBeverage)
    }

    @BindingAdapter("app:graphValueSets")
    @JvmStatic
    fun graphValueSets(graphView: GraphView, graphValueSets: List<GraphValueSet>?) {
        Log.d("TAG", "graphValueSets: ${graphValueSets?.size}")
        graphView.setGraphValueSets(graphValueSets)
    }

    @BindingAdapter("app:onValueClick")
    @JvmStatic
    fun onValueClick(graphView: GraphView, listener: OnValueClickListener?) {
        graphView.setOnValueClickListener(listener)
    }

    @BindingAdapter("app:graphValue")
    @JvmStatic
    fun graphValue(parent: ViewGroup, graphValue: GraphValue?) {
        val view = parent.findViewById<TextView>(R.id.timeTextView)

        if (graphValue is CounterEntry) {
            view.text = TimeUtils.toTime(graphValue.time)
        }
    }

    @BindingAdapter("app:graphValueSet")
    @JvmStatic
    fun graphValueSet(parent: ViewGroup, graphValueSet: GraphValueSet?) {
        val context = parent.context
        val view = parent.findViewById<TextView>(R.id.beverageNameTextView)

        if (graphValueSet is CounterWithBeverage) {
            val volume = TextUtils.decimalToStringWithTwoDecimalDigits(graphValueSet.counter.volume.toDouble())
            view.text = "${graphValueSet.beverage.name} $volume${context.resources.getString(R.string.liter)}"
            view.setTextColor(graphValueSet.beverage.textColor)

            parent.setBackgroundColor(graphValueSet.color)

            val timeTextView = parent.findViewById<TextView>(R.id.timeTextView)
            timeTextView.setTextColor(graphValueSet.beverage.textColor)
        }
    }
}