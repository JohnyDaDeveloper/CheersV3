package cz.johnyapps.cheers.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import cz.johnyapps.cheers.R
import cz.johnyapps.cheers.adapters.BaseAdapter
import cz.johnyapps.cheers.databinding.ViewEmptyMessageRecyclerViewBinding

class EmptyMessageRecyclerView: LinearLayout {
    private lateinit var binding: ViewEmptyMessageRecyclerViewBinding

    constructor(context: Context) : super(context) {
        initialize(null, 0)
    }

    constructor(context: Context, attrs: AttributeSet?): super(context, attrs) {
        initialize(attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet?, theme: Int): super(context, attrs, theme) {
        initialize(attrs, theme)
    }

    private fun initialize(attrs: AttributeSet?, theme: Int) {
        var emptyMessage: String? = null

        if (attrs != null) {
            val array = context.obtainStyledAttributes(attrs, R.styleable.EmptyMessageRecyclerView, theme, 0)

            emptyMessage = array.getString(R.styleable.EmptyMessageRecyclerView_emptyMessage)

            array.recycle()
        }

        binding = DataBindingUtil.inflate(LayoutInflater.from(context),
            R.layout.view_empty_message_recycler_view,
            this,
            true)
        binding.emptyMessageTextView.text = emptyMessage
    }

    fun setAdapter(adapter: RecyclerView.Adapter<*>, layoutManager: RecyclerView.LayoutManager) {
        if (adapter is BaseAdapter<*, *>) {
            adapter.onListChangeListener = object : BaseAdapter.OnListChangedListener {
                override fun onChange(isListEmpty: Boolean) {
                    if (isListEmpty) {
                        binding.recyclerView.visibility = View.GONE
                        binding.emptyMessageLayout.visibility = View.VISIBLE
                    } else {
                        binding.recyclerView.visibility = View.VISIBLE
                        binding.emptyMessageLayout.visibility = View.GONE
                    }
                }
            }
        }

        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = adapter
    }

    fun scrollToPosition(position: Int) {
        binding.recyclerView.scrollToPosition(position)
    }
}