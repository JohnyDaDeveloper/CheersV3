package cz.johnyapps.cheers.adapters

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

abstract class BaseAdapter<T, VH: BaseAdapter<T, VH>.ViewHolder>(itemCallback: DiffUtil.ItemCallback<T>): ListAdapter<T, VH>(itemCallback) {
    var onListChangeListener: OnListChangedListener? = null

    fun getContext(viewHolder: VH): Context {
        return viewHolder.itemView.context
    }

    override fun onCurrentListChanged(previousList: MutableList<T>, currentList: MutableList<T>) {
        super.onCurrentListChanged(previousList, currentList)
        onListChangeListener?.onChange(currentList.isEmpty())
    }

    interface OnRootClickListener {
        fun onClick(view: View)
    }

    interface OnListChangedListener {
        fun onChange(isListEmpty: Boolean)
    }

    open inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val rootOnClickListeners = ArrayList<OnRootClickListener>()

        init {
            itemView.setOnClickListener {
                for (listener in rootOnClickListeners) {
                    listener.onClick(it)
                }
            }
        }

        fun addRootOnClickListener(listener: OnRootClickListener) {
            rootOnClickListeners.add(listener)
        }
    }
}