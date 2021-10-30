package cz.johnyapps.cheers.adapters

import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.DiffUtil
import cz.johnyapps.cheers.tools.TextUtils

abstract class FilterableAdapter<T, VH: SelectableAdapter<T, VH>.ViewHolder>(itemCallback: DiffUtil.ItemCallback<T>): ExpandableListAdapter<T, VH>(itemCallback), Filterable {
    abstract fun getItemTextForFilter(item: T): String
    private var ignoreNextSubmit = false
    private var items: List<T> = ArrayList()
    private var filteredItems: List<T>? = null

    fun filterItem(item: T, constraint: CharSequence): Boolean {
        return TextUtils.removeDiacritics(getItemTextForFilter(item).lowercase())
            .toString()
            .contains(constraint)
    }

    fun filter(query: CharSequence) {
        filter.filter(query)
    }

    fun clearFilter() {
        filteredItems = null
        submitList(items)
    }

    fun isFiltered(): Boolean {
        return filteredItems != null
    }

    override fun submitList(list: List<T>?) {
        if (!ignoreNextSubmit) {
            items = list ?: ArrayList()
        }

        super.submitList(list)
    }

    override fun submitList(list: List<T>?, commitCallback: Runnable?) {
        if (!ignoreNextSubmit) {
            items = list ?: ArrayList()
        }

        super.submitList(list, commitCallback)
    }

    override fun getFilter(): Filter {
        return object: Filter() {
            override fun performFiltering(constraint: CharSequence): FilterResults {
                val filterBy = TextUtils.removeDiacritics(constraint.toString().lowercase())
                val filtered: MutableList<T> = ArrayList()

                for (item in items) {
                    if (filterItem(item, filterBy)) {
                        filtered.add(item)
                    }
                }

                val filterResults = FilterResults()
                filterResults.values = filtered
                filterResults.count = filtered.size
                return filterResults
            }

            override fun publishResults(constraint: CharSequence, results: FilterResults) {
                ignoreNextSubmit = true
                filteredItems = results.values as MutableList<T>
                submitList(filteredItems)
            }
        }
    }
}