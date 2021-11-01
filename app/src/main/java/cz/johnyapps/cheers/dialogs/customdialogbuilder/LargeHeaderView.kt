package cz.johnyapps.cheers.dialogs.customdialogbuilder

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatTextView
import android.view.LayoutInflater
import cz.johnyapps.cheers.R

class LargeHeaderView : LinearLayout {
    private var titleTextView: AppCompatTextView? = null

    constructor(context: Context) : super(context) {
        init(null, 0)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet?, theme: Int) : super(context, attrs, theme) {
        init(attrs, theme)
    }

    fun init(attrs: AttributeSet?, theme: Int) {
        LayoutInflater.from(context).inflate(R.layout.view_large_header, this, true)
        titleTextView = findViewById(R.id.titleTextView)
    }

    fun setTitle(title: CharSequence?) {
        if (titleTextView != null) {
            titleTextView!!.text = title
        }
    }
}