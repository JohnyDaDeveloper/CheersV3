package cz.johnyapps.cheers.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import cz.johnyapps.cheers.R
import cz.johnyapps.cheers.databinding.ItemCounterBinding
import cz.johnyapps.cheers.entities.CounterWithBeverage

class CountersAdapter: SelectableAdapter<CounterWithBeverage, CountersAdapter.ViewHolder>(DIFF_CALLBACK) {
    var allCountersDisabled = false
        set(value) {
            notifyDataSetChanged()
            field = value
        }
    var onCounterClickListener: OnCounterClickListener? = null

    init {
        setHasStableIds(true)
    }

    override fun getItemId(position: Int): Long {
        return getItem(position).counter.id
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, selected: Boolean) {
        holder.binding.counterWithBeverage = getItem(position)
        holder.binding.counterView.setPassClicks(allCountersDisabled)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context),
            R.layout.item_counter,
            parent,
            false))
    }

    open inner class ViewHolder(val binding: ItemCounterBinding): SelectableAdapter<CounterWithBeverage, CountersAdapter.ViewHolder>.ViewHolder(binding.root) {
        init {
            addRootOnClickListener(object : OnRootClickListener {
                override fun onClick(view: View) {
                    if (allCountersDisabled) {
                        onCounterClickListener?.onClick(getItem(adapterPosition))
                    }
                }
            })
        }
    }

    companion object {
        private val DIFF_CALLBACK: DiffUtil.ItemCallback<CounterWithBeverage> = object: DiffUtil.ItemCallback<CounterWithBeverage>(){
            override fun areItemsTheSame(oldItem: CounterWithBeverage, newItem: CounterWithBeverage): Boolean {
                return oldItem.counter.id == newItem.counter.id
            }

            override fun areContentsTheSame(oldItem: CounterWithBeverage, newItem: CounterWithBeverage): Boolean {
                return oldItem.counter == newItem.counter
            }

        }
    }

    interface OnCounterClickListener {
        fun onClick(counterWithBeverage: CounterWithBeverage)
    }
}