package cz.johnyapps.cheers.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import cz.johnyapps.cheers.R
import cz.johnyapps.cheers.databinding.ItemCounterBinding
import cz.johnyapps.cheers.entities.CounterWithBeverage

class CountersAdapter: SelectableAdapter<CounterWithBeverage, CountersAdapter.ViewHolder>(DIFF_CALLBACK) {
    override fun onBindViewHolder(holder: ViewHolder, position: Int, selected: Boolean) {
        holder.binding.counterWithBeverage = getItem(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context),
            R.layout.item_counter,
            parent,
            false))
    }


    open inner class ViewHolder(val binding: ItemCounterBinding): SelectableAdapter<CounterWithBeverage, CountersAdapter.ViewHolder>.ViewHolder(binding.root)

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
}