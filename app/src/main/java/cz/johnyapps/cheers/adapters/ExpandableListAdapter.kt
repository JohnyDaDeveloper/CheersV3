package cz.johnyapps.cheers.adapters

import android.view.View
import androidx.recyclerview.widget.DiffUtil

abstract class ExpandableListAdapter<T, VH: SelectableAdapter<T, VH>.ViewHolder>(itemCallback: DiffUtil.ItemCallback<T>): SelectableAdapter<T, VH>(itemCallback) {
    private var expandedItemPosition: Int = -1

    fun expandPosition(position: Int, notify: Boolean) {
        val oldPosition: Int = expandedItemPosition
        expandedItemPosition = if (oldPosition == position) -1 else position

        if (notify) {
            notifyItemChanged(oldPosition)
            notifyItemChanged(position)
        }
    }

    fun isExpanded(position: Int): Boolean {
        return position == expandedItemPosition
    }

    override fun onBindViewHolder(holder: VH, position: Int, selected: Boolean) {
        val expanded = isExpanded(position)

        if (selected && expanded) {
            expandPosition(position, false)
        }

        onBindViewHolder(holder, position, selected, expanded)
    }

    abstract fun onBindViewHolder(holder: VH, position: Int, selected: Boolean, expanded: Boolean)

    open inner class ViewHolder(itemView: View): SelectableAdapter<T, VH>.ViewHolder(itemView) {
        init {
            addRootOnClickListener(object : OnRootClickListener {
                override fun onClick(view: View) {
                    if (!isSelecting() && !canceledSelectionThisClick()) {
                        expandPosition(adapterPosition, true)
                    }
                }
            })
        }
    }
}