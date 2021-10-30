package cz.johnyapps.cheers.adapters

import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.DiffUtil
import cz.johnyapps.cheers.R

import java.util.*
import kotlin.collections.Collection
import kotlin.collections.HashMap
import kotlin.collections.isNotEmpty
import kotlin.collections.set
import kotlin.collections.toTypedArray

abstract class SelectableAdapter<T, VH: SelectableAdapter<T, VH>.ViewHolder>(itemCallback: DiffUtil.ItemCallback<T>): BaseAdapter<T, VH>(itemCallback) {
    private val selectedItems = HashMap<Int, T>()
    private var multiSelection = true
    private var allowSelection = true
    private var canceledSelectionThisClick = false
    var onSelectListener: OnSelectListener<T>? = null

    fun isSelecting(): Boolean {
        return selectedItems.isNotEmpty()
    }

    open fun isSelected(position: Int): Boolean {
        return selectedItems[position] != null
    }

    open fun selectPosition(pos: Int) {
        if (!allowSelection) {
            return
        }

        val oldItemPos = if (selectedItems.keys.isEmpty()) -1 else selectedItems.keys.toTypedArray()[0]

        val wasAlreadySelected: Boolean = isSelected(pos)

        if (wasAlreadySelected || pos < 0) {
            selectedItems.remove(pos)
        } else {
            if (!multiSelection && oldItemPos >= 0) {
                selectedItems.remove(oldItemPos)
                notifyItemChanged(oldItemPos)
            }

            val selectedItem: T = getItem(pos)
            selectedItems[pos] = selectedItem
        }

        notifyItemChanged(pos)

        onSelectListener?.onSelect(selectedItems.values)
    }

    open fun cancelSelection() {
        val positions: Collection<Int> = ArrayList(selectedItems.keys)
        for (pos in positions) {
            selectPosition(pos)
        }
    }

    open fun selectAllOrCancelSelection() {
        var allSelected = true

        for (i in 0 until itemCount) {
            if (!isSelected(i)) {
                allSelected = false
                selectPosition(i)
            }
        }

        if (allSelected) {
            cancelSelection()
        }
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val selected = isSelected(position)

        holder.itemView.setOnLongClickListener {
            selectPosition(position)
            true
        }

        if (selected) {
            holder.itemView.foreground = ResourcesCompat.getDrawable(
                getContext(holder).resources,
                R.drawable.selected_item_background,
                getContext(holder).theme
            )
        } else {
            holder.itemView.foreground = null
        }

        onBindViewHolder(holder, position, selected)
    }

    abstract fun onBindViewHolder(holder: VH, position: Int, selected: Boolean)

    open fun canceledSelectionThisClick(): Boolean {
        return canceledSelectionThisClick
    }

    open fun setAllowSelection(allowSelection: Boolean) {
        this.allowSelection = allowSelection
    }

    open fun isAllowSelection(): Boolean {
        return allowSelection
    }

    open fun setMultiSelection(multiSelection: Boolean) {
        this.multiSelection = multiSelection
    }

    open inner class ViewHolder(itemView: View): BaseAdapter<T, VH>.ViewHolder(itemView) {
        init {
            itemView.setOnLongClickListener {
                selectPosition(adapterPosition)
                false
            }

            addRootOnClickListener(object : RootOnClickListener {
                override fun onClick(view: View) {
                    canceledSelectionThisClick = if (isSelecting()) {
                        selectPosition(adapterPosition)
                        selectedItems.isEmpty()
                    } else {
                        false
                    }
                }
            })
        }
    }

    interface OnSelectListener<T> {
        fun onSelect(selectedItems: Collection<T>?)
    }
}