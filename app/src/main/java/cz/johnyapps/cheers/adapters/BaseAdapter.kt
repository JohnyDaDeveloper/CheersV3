package cz.johnyapps.cheers.adapters

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

abstract class BaseAdapter<T, VH: BaseAdapter<T, VH>.ViewHolder>(itemCallback: DiffUtil.ItemCallback<T>): ListAdapter<T, VH>(itemCallback) {
    fun getContext(viewHolder: VH): Context {
        return viewHolder.itemView.context
    }

    open inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val rootOnClickListeners = ArrayList<RootOnClickListener>()

        init {
            itemView.setOnClickListener {
                for (listener in rootOnClickListeners) {
                    listener.onClick(it)
                }
            }
        }

        fun addRootOnClickListener(listener: RootOnClickListener) {
            rootOnClickListeners.add(listener)
        }
    }

    interface RootOnClickListener {
        fun onClick(view: View)
    }
}