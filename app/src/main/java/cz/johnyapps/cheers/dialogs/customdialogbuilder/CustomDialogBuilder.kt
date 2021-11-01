package cz.johnyapps.cheers.dialogs.customdialogbuilder

import android.content.Context
import android.content.DialogInterface
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import android.graphics.drawable.Drawable
import androidx.annotation.StringRes
import androidx.annotation.DrawableRes
import androidx.annotation.AttrRes
import androidx.annotation.ArrayRes
import android.content.DialogInterface.OnMultiChoiceClickListener
import android.database.Cursor
import android.view.View
import android.widget.AdapterView
import android.widget.ListAdapter
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AlertDialog

class CustomDialogBuilder(context: Context) : MaterialAlertDialogBuilder(context) {
    private var largeHeaderView: LargeHeaderView? = null
    private var title: CharSequence? = null
    private var titleChanged = false
    fun showLargeHeader(): CustomDialogBuilder {
        largeHeaderView = LargeHeaderView(context)
        if (titleChanged) {
            largeHeaderView!!.setTitle(title)
        }
        setCustomTitle(largeHeaderView)
        return this
    }

    override fun setBackground(background: Drawable?): CustomDialogBuilder {
        super.setBackground(background)
        return this
    }

    override fun setBackgroundInsetStart(backgroundInsetStart: Int): CustomDialogBuilder {
        super.setBackgroundInsetStart(backgroundInsetStart)
        return this
    }

    override fun setBackgroundInsetTop(backgroundInsetTop: Int): CustomDialogBuilder {
        super.setBackgroundInsetTop(backgroundInsetTop)
        return this
    }

    override fun setBackgroundInsetEnd(backgroundInsetEnd: Int): CustomDialogBuilder {
        super.setBackgroundInsetEnd(backgroundInsetEnd)
        return this
    }

    override fun setBackgroundInsetBottom(backgroundInsetBottom: Int): CustomDialogBuilder {
        super.setBackgroundInsetBottom(backgroundInsetBottom)
        return this
    }

    override fun setTitle(@StringRes titleId: Int): CustomDialogBuilder {
        setTitle(context.resources.getString(titleId))
        return this
    }

    override fun setTitle(title: CharSequence?): CustomDialogBuilder {
        this.title = title
        titleChanged = true
        if (largeHeaderView != null) {
            largeHeaderView!!.setTitle(title)
        }
        super.setTitle(title)
        return this
    }

    override fun setCustomTitle(customTitleView: View?): CustomDialogBuilder {
        super.setCustomTitle(customTitleView)
        return this
    }

    override fun setMessage(@StringRes messageId: Int): CustomDialogBuilder {
        super.setMessage(messageId)
        return this
    }

    override fun setMessage(message: CharSequence?): CustomDialogBuilder {
        super.setMessage(message)
        return this
    }

    override fun setIcon(@DrawableRes iconId: Int): CustomDialogBuilder {
        super.setIcon(iconId)
        return this
    }

    override fun setIcon(icon: Drawable?): CustomDialogBuilder {
        super.setIcon(icon)
        return this
    }

    override fun setIconAttribute(@AttrRes attrId: Int): CustomDialogBuilder {
        super.setIconAttribute(attrId)
        return this
    }

    override fun setPositiveButton(
        @StringRes textId: Int,
        listener: DialogInterface.OnClickListener?
    ): CustomDialogBuilder {
        super.setPositiveButton(textId, listener)
        return this
    }

    override fun setPositiveButton(
        text: CharSequence?,
        listener: DialogInterface.OnClickListener?
    ): CustomDialogBuilder {
        super.setPositiveButton(text, listener)
        return this
    }

    override fun setPositiveButtonIcon(icon: Drawable?): CustomDialogBuilder {
        super.setPositiveButtonIcon(icon)
        return this
    }

    override fun setNegativeButton(
        @StringRes textId: Int,
        listener: DialogInterface.OnClickListener?
    ): CustomDialogBuilder {
        super.setNegativeButton(textId, listener)
        return this
    }

    override fun setNegativeButton(
        text: CharSequence?,
        listener: DialogInterface.OnClickListener?
    ): CustomDialogBuilder {
        super.setNegativeButton(text, listener)
        return this
    }

    override fun setNegativeButtonIcon(icon: Drawable?): CustomDialogBuilder {
        super.setNegativeButtonIcon(icon)
        return this
    }

    override fun setNeutralButton(
        @StringRes textId: Int,
        listener: DialogInterface.OnClickListener?
    ): CustomDialogBuilder {
        super.setNeutralButton(textId, listener)
        return this
    }

    override fun setNeutralButton(
        text: CharSequence?,
        listener: DialogInterface.OnClickListener?
    ): CustomDialogBuilder {
        super.setNeutralButton(text, listener)
        return this
    }

    override fun setNeutralButtonIcon(icon: Drawable?): CustomDialogBuilder {
        super.setNeutralButtonIcon(icon)
        return this
    }

    override fun setCancelable(cancelable: Boolean): CustomDialogBuilder {
        super.setCancelable(cancelable)
        return this
    }

    override fun setOnCancelListener(onCancelListener: DialogInterface.OnCancelListener?): CustomDialogBuilder {
        super.setOnCancelListener(onCancelListener)
        return this
    }

    override fun setOnDismissListener(onDismissListener: DialogInterface.OnDismissListener?): CustomDialogBuilder {
        super.setOnDismissListener(onDismissListener)
        return this
    }

    override fun setOnKeyListener(onKeyListener: DialogInterface.OnKeyListener?): CustomDialogBuilder {
        super.setOnKeyListener(onKeyListener)
        return this
    }

    override fun setItems(
        @ArrayRes itemsId: Int,
        listener: DialogInterface.OnClickListener?
    ): CustomDialogBuilder {
        super.setItems(itemsId, listener)
        return this
    }

    override fun setItems(
        items: Array<CharSequence>?,
        listener: DialogInterface.OnClickListener?
    ): CustomDialogBuilder {
        super.setItems(items, listener)
        return this
    }

    override fun setAdapter(
        adapter: ListAdapter?,
        listener: DialogInterface.OnClickListener?
    ): CustomDialogBuilder {
        super.setAdapter(adapter, listener)
        return this
    }

    override fun setCursor(
        cursor: Cursor?,
        listener: DialogInterface.OnClickListener?,
        labelColumn: String
    ): CustomDialogBuilder {
        super.setCursor(cursor, listener, labelColumn)
        return this
    }

    override fun setMultiChoiceItems(
        @ArrayRes itemsId: Int,
        checkedItems: BooleanArray?,
        listener: OnMultiChoiceClickListener?
    ): CustomDialogBuilder {
        super.setMultiChoiceItems(itemsId, checkedItems, listener)
        return this
    }

    override fun setMultiChoiceItems(
        items: Array<CharSequence>?,
        checkedItems: BooleanArray?,
        listener: OnMultiChoiceClickListener?
    ): CustomDialogBuilder {
        super.setMultiChoiceItems(items, checkedItems, listener)
        return this
    }

    override fun setMultiChoiceItems(
        cursor: Cursor?,
        isCheckedColumn: String,
        labelColumn: String,
        listener: OnMultiChoiceClickListener?
    ): CustomDialogBuilder {
        super.setMultiChoiceItems(cursor, isCheckedColumn, labelColumn, listener)
        return this
    }

    override fun setSingleChoiceItems(
        @ArrayRes itemsId: Int,
        checkedItem: Int,
        listener: DialogInterface.OnClickListener?
    ): CustomDialogBuilder {
        super.setSingleChoiceItems(itemsId, checkedItem, listener)
        return this
    }

    override fun setSingleChoiceItems(
        cursor: Cursor?,
        checkedItem: Int,
        labelColumn: String,
        listener: DialogInterface.OnClickListener?
    ): CustomDialogBuilder {
        super.setSingleChoiceItems(cursor, checkedItem, labelColumn, listener)
        return this
    }

    override fun setSingleChoiceItems(
        items: Array<CharSequence>?,
        checkedItem: Int,
        listener: DialogInterface.OnClickListener?
    ): CustomDialogBuilder {
        super.setSingleChoiceItems(items, checkedItem, listener)
        return this
    }

    override fun setSingleChoiceItems(
        adapter: ListAdapter?,
        checkedItem: Int,
        listener: DialogInterface.OnClickListener?
    ): CustomDialogBuilder {
        super.setSingleChoiceItems(adapter, checkedItem, listener)
        return this
    }

    override fun setOnItemSelectedListener(listener: AdapterView.OnItemSelectedListener?): CustomDialogBuilder {
        super.setOnItemSelectedListener(listener)
        return this
    }

    override fun setView(view: View?): CustomDialogBuilder {
        super.setView(view)
        return this
    }

    override fun setView(@LayoutRes layoutResId: Int): CustomDialogBuilder {
        super.setView(layoutResId)
        return this
    }
}