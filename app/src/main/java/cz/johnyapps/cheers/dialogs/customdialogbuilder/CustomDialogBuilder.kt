package cz.johnyapps.cheers.dialogs.customdialogbuilder;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;

import androidx.annotation.ArrayRes;
import androidx.annotation.AttrRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AlertDialog;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class CustomDialogBuilder extends MaterialAlertDialogBuilder {
    @Nullable
    private LargeHeaderView largeHeaderView;
    @Nullable
    private CharSequence title;
    private boolean titleChanged = false;

    public CustomDialogBuilder(@NonNull Context context) {
        super(context);
    }

    @NonNull
    public CustomDialogBuilder showLargeHeader() {
        largeHeaderView = new LargeHeaderView(getContext());

        if (titleChanged) {
            largeHeaderView.setTitle(title);
        }

        setCustomTitle(largeHeaderView);
        return this;
    }

    @NonNull
    @Override
    public CustomDialogBuilder setBackground(@Nullable Drawable background) {
        super.setBackground(background);
        return this;
    }

    @NonNull
    @Override
    public CustomDialogBuilder setBackgroundInsetStart(int backgroundInsetStart) {
        super.setBackgroundInsetStart(backgroundInsetStart);
        return this;
    }

    @NonNull
    @Override
    public CustomDialogBuilder setBackgroundInsetTop(int backgroundInsetTop) {
        super.setBackgroundInsetTop(backgroundInsetTop);
        return this;
    }

    @NonNull
    @Override
    public CustomDialogBuilder setBackgroundInsetEnd(int backgroundInsetEnd) {
        super.setBackgroundInsetEnd(backgroundInsetEnd);
        return this;
    }

    @NonNull
    @Override
    public CustomDialogBuilder setBackgroundInsetBottom(int backgroundInsetBottom) {
        super.setBackgroundInsetBottom(backgroundInsetBottom);
        return this;
    }

    @NonNull
    @Override
    public CustomDialogBuilder setTitle(@StringRes int titleId) {
        setTitle(getContext().getResources().getString(titleId));
        return this;
    }

    @NonNull
    @Override
    public CustomDialogBuilder setTitle(@Nullable CharSequence title) {
        this.title = title;
        this.titleChanged = true;

        if (largeHeaderView != null) {
            largeHeaderView.setTitle(title);
        }

        super.setTitle(title);
        return this;
    }

    @NonNull
    @Override
    public CustomDialogBuilder setCustomTitle(@Nullable View customTitleView) {
        super.setCustomTitle(customTitleView);
        return this;
    }

    @NonNull
    @Override
    public CustomDialogBuilder setMessage(@StringRes int messageId) {
        super.setMessage(messageId);
        return this;
    }

    @NonNull
    @Override
    public CustomDialogBuilder setMessage(@Nullable CharSequence message) {
        super.setMessage(message);
        return this;
    }

    @NonNull
    @Override
    public CustomDialogBuilder setIcon(@DrawableRes int iconId) {
        super.setIcon(iconId);
        return this;
    }

    @NonNull
    @Override
    public CustomDialogBuilder setIcon(@Nullable Drawable icon) {
        super.setIcon(icon);
        return this;
    }

    @NonNull
    @Override
    public CustomDialogBuilder setIconAttribute(@AttrRes int attrId) {
        super.setIconAttribute(attrId);
        return this;
    }

    @NonNull
    @Override
    public CustomDialogBuilder setPositiveButton(@StringRes int textId,
                                                 @Nullable DialogInterface.OnClickListener listener) {
        super.setPositiveButton(textId, listener);
        return this;
    }

    @NonNull
    @Override
    public CustomDialogBuilder setPositiveButton(@Nullable CharSequence text,
                                                 @Nullable DialogInterface.OnClickListener listener) {
        super.setPositiveButton(text, listener);
        return this;
    }

    @NonNull
    @Override
    public CustomDialogBuilder setPositiveButtonIcon(@Nullable Drawable icon) {
        super.setPositiveButtonIcon(icon);
        return this;
    }

    @NonNull
    @Override
    public CustomDialogBuilder setNegativeButton(@StringRes int textId,
                                                 @Nullable DialogInterface.OnClickListener listener) {
        super.setNegativeButton(textId, listener);
        return this;
    }

    @NonNull
    @Override
    public CustomDialogBuilder setNegativeButton(@Nullable CharSequence text,
                                                 @Nullable DialogInterface.OnClickListener listener) {
        super.setNegativeButton(text, listener);
        return this;
    }

    @NonNull
    @Override
    public CustomDialogBuilder setNegativeButtonIcon(@Nullable Drawable icon) {
        super.setNegativeButtonIcon(icon);
        return this;
    }

    @NonNull
    @Override
    public CustomDialogBuilder setNeutralButton(@StringRes int textId,
                                                @Nullable DialogInterface.OnClickListener listener) {
        super.setNeutralButton(textId, listener);
        return this;
    }

    @NonNull
    @Override
    public CustomDialogBuilder setNeutralButton(@Nullable CharSequence text,
                                                @Nullable DialogInterface.OnClickListener listener) {
        super.setNeutralButton(text, listener);
        return this;
    }

    @NonNull
    @Override
    public CustomDialogBuilder setNeutralButtonIcon(@Nullable Drawable icon) {
        super.setNeutralButtonIcon(icon);
        return this;
    }

    @NonNull
    @Override
    public CustomDialogBuilder setCancelable(boolean cancelable) {
        super.setCancelable(cancelable);
        return this;
    }

    @NonNull
    @Override
    public CustomDialogBuilder setOnCancelListener(@Nullable DialogInterface.OnCancelListener onCancelListener) {
        super.setOnCancelListener(onCancelListener);
        return this;
    }

    @NonNull
    @Override
    public CustomDialogBuilder setOnDismissListener(@Nullable DialogInterface.OnDismissListener onDismissListener) {
        super.setOnDismissListener(onDismissListener);
        return this;
    }

    @NonNull
    @Override
    public CustomDialogBuilder setOnKeyListener(@Nullable DialogInterface.OnKeyListener onKeyListener) {
        super.setOnKeyListener(onKeyListener);
        return this;
    }

    @NonNull
    @Override
    public CustomDialogBuilder setItems(@ArrayRes int itemsId,
                                        @Nullable DialogInterface.OnClickListener listener) {
        super.setItems(itemsId, listener);
        return this;
    }

    @NonNull
    @Override
    public CustomDialogBuilder setItems(@Nullable CharSequence[] items,
                                        @Nullable DialogInterface.OnClickListener listener) {
        super.setItems(items, listener);
        return this;
    }

    @NonNull
    @Override
    public CustomDialogBuilder setAdapter(@Nullable ListAdapter adapter,
                                          @Nullable DialogInterface.OnClickListener listener) {
        super.setAdapter(adapter, listener);
        return this;
    }

    @NonNull
    @Override
    public CustomDialogBuilder setCursor(@Nullable Cursor cursor,
                                         @Nullable DialogInterface.OnClickListener listener,
                                         @NonNull String labelColumn) {
        super.setCursor(cursor, listener, labelColumn);
        return this;
    }

    @NonNull
    @Override
    public CustomDialogBuilder setMultiChoiceItems(@ArrayRes int itemsId,
                                                   @Nullable boolean[] checkedItems,
                                                   @Nullable DialogInterface.OnMultiChoiceClickListener listener) {
        super.setMultiChoiceItems(itemsId, checkedItems, listener);
        return this;
    }

    @NonNull
    @Override
    public CustomDialogBuilder setMultiChoiceItems(@Nullable CharSequence[] items,
                                                   @Nullable boolean[] checkedItems,
                                                   @Nullable DialogInterface.OnMultiChoiceClickListener listener) {
        super.setMultiChoiceItems(items, checkedItems, listener);
        return this;
    }

    @NonNull
    @Override
    public CustomDialogBuilder setMultiChoiceItems(@Nullable Cursor cursor,
                                                   @NonNull String isCheckedColumn,
                                                   @NonNull String labelColumn,
                                                   @Nullable DialogInterface.OnMultiChoiceClickListener listener) {
        super.setMultiChoiceItems(cursor, isCheckedColumn, labelColumn, listener);
        return this;
    }

    @NonNull
    @Override
    public CustomDialogBuilder setSingleChoiceItems(@ArrayRes int itemsId,
                                                    int checkedItem,
                                                    @Nullable DialogInterface.OnClickListener listener) {
        super.setSingleChoiceItems(itemsId, checkedItem, listener);
        return this;
    }

    @NonNull
    @Override
    public CustomDialogBuilder setSingleChoiceItems(@Nullable Cursor cursor,
                                                    int checkedItem,
                                                    @NonNull String labelColumn,
                                                    @Nullable DialogInterface.OnClickListener listener) {
        super.setSingleChoiceItems(cursor, checkedItem, labelColumn, listener);
        return this;
    }

    @NonNull
    @Override
    public CustomDialogBuilder setSingleChoiceItems(@Nullable CharSequence[] items,
                                                    int checkedItem,
                                                    @Nullable DialogInterface.OnClickListener listener) {
        super.setSingleChoiceItems(items, checkedItem, listener);
        return this;
    }

    @NonNull
    @Override
    public CustomDialogBuilder setSingleChoiceItems(@Nullable ListAdapter adapter,
                                                    int checkedItem,
                                                    @Nullable DialogInterface.OnClickListener listener) {
        super.setSingleChoiceItems(adapter, checkedItem, listener);
        return this;
    }

    @NonNull
    @Override
    public CustomDialogBuilder setOnItemSelectedListener(@Nullable AdapterView.OnItemSelectedListener listener) {
        super.setOnItemSelectedListener(listener);
        return this;
    }

    @NonNull
    @Override
    public CustomDialogBuilder setView(@Nullable View view) {
        super.setView(view);
        return this;
    }

    @NonNull
    @Override
    public CustomDialogBuilder setView(@LayoutRes int layoutResId) {
        super.setView(layoutResId);
        return this;
    }

    @NonNull
    @Override
    public AlertDialog create() {
        return super.create();
    }

    @Override
    public AlertDialog show() {
        return super.show();
    }
}
