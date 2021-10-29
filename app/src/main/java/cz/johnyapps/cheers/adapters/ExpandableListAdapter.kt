package cz.johnyapps.cheers.adapters

import android.view.View
import androidx.recyclerview.widget.DiffUtil

abstract class ExpandableListAdapter<T, VH: ExpandableListAdapter<T, VH>.ExpandableViewHolder>(itemCallback: DiffUtil.ItemCallback<T>): BaseAdapter<T, VH>(itemCallback) {
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

    open inner class ExpandableViewHolder(itemView: View): BaseAdapter<T, VH>.ViewHolder(itemView) {
        init {
            addRootOnClickListener(object : RootOnClickListener {
                override fun onClick(view: View) {
                    selectItem(adapterPosition)
                }
            })
        }
    }
}