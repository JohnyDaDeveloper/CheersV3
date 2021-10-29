package cz.johnyapps.cheers.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import cz.johnyapps.cheers.R
import cz.johnyapps.cheers.databinding.ItemBeverageNewBinding
import cz.johnyapps.cheers.entities.beverage.Beverage

class BeveragesAdapterNew: ListAdapter<Beverage, BeveragesAdapterNew.ViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_beverage_new, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.beverage = getItem(position)
    }

    class ViewHolder(val binding: ItemBeverageNewBinding) : RecyclerView.ViewHolder(binding.root)

    companion object {
        private val DIFF_CALLBACK: DiffUtil.ItemCallback<Beverage> = object: DiffUtil.ItemCallback<Beverage>(){
            override fun areItemsTheSame(oldItem: Beverage, newItem: Beverage): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Beverage, newItem: Beverage): Boolean {
                return oldItem == newItem
            }

        }
    }
}