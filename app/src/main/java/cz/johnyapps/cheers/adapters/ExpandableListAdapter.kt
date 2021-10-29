package cz.johnyapps.cheers.adapters

import android.view.View
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

abstract class ExpandableListAdapter<T, VH: ExpandableListAdapter<T, VH>.ExpandableViewHolder>(itemCallback: DiffUtil.ItemCallback<T>): ListAdapter<T, VH>(itemCallback) {
    private var expandedItemPosition: Int = -1

    fun selectItem(position: Int) {
        val oldPosition: Int = expandedItemPosition
        expandedItemPosition = if (oldPosition == position) -1 else position
        notifyItemChanged(oldPosition)
        notifyItemChanged(position)
    }

    fun isExpanded(position: Int): Boolean {
        return position == expandedItemPosition
    }

    abstract inner class ExpandableViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        init {
            itemView.setOnClickListener { selectItem(adapterPosition) }
        }
    }
}