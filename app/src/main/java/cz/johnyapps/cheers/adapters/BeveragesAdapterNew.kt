package cz.johnyapps.cheers.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil

import cz.johnyapps.cheers.R
import cz.johnyapps.cheers.database.tasks.BaseDatabaseTask
import cz.johnyapps.cheers.database.tasks.GetBeverageDescriptionTask
import cz.johnyapps.cheers.databinding.ItemBeverageNewBinding
import cz.johnyapps.cheers.entities.beverage.Beverage
import cz.johnyapps.cheers.tools.TextUtils
import java.lang.Exception

class BeveragesAdapterNew: ExpandableListAdapter<Beverage, BeveragesAdapterNew.ViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_beverage_new, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.beverage = getItem(position)
        holder.binding.descriptionLayout.visibility = if (isExpanded(position)) View.VISIBLE else View.GONE

        if (isExpanded(position)) {
            val task = GetBeverageDescriptionTask(holder.binding.root.context)
            task.setOnCompleteListener(object :
                BaseDatabaseTask.OnCompleteListener<GetBeverageDescriptionTask.Result?> {
                override fun onSuccess(result: GetBeverageDescriptionTask.Result?) {
                    if (isExpanded(holder.adapterPosition)) {
                        fillDetails(holder.binding.descriptionTextView,
                            getItem(holder.adapterPosition),
                            result)
                    }
                }

                override fun onFailure(e: Exception?) {}
                override fun onComplete() {}
            })
            task.execute(holder.binding.beverage)
        }
    }

    private fun fillDetails(descriptionTextView: AppCompatTextView,
        beverage: Beverage,
        result: GetBeverageDescriptionTask.Result?) {
        var text = ""

        if (beverage.alcohol > 0) {
            text += descriptionTextView.context.resources.getString(
                R.string.beverage_recycler_view_item_description_alcohol,
                TextUtils.decimalToStringWithTwoDecimalDigits(beverage.alcohol.toDouble())
            )
            text += "\n"
        }

        text += if (result != null) {
            descriptionTextView.context.resources.getString(
                R.string.beverage_recycler_view_item_description_rest,
                TextUtils.decimalToStringWithTwoDecimalDigits(result.lastVisit),
                TextUtils.decimalToStringWithTwoDecimalDigits(result.lastVisitVolume.toDouble()),
                TextUtils.decimalToStringWithTwoDecimalDigits(result.total)
            )
        } else {
            descriptionTextView.context.resources
                .getString(R.string.beverage_recycler_view_item_description_no_data)
        }
        descriptionTextView.text = text
    }

    inner class ViewHolder(val binding: ItemBeverageNewBinding) : ExpandableListAdapter<Beverage, ViewHolder>.ExpandableViewHolder(binding.root)

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