package cz.johnyapps.cheers.dialogs.customdialogbuilder;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import cz.johnyapps.cheers.R;

public class LargeHeaderView extends LinearLayout {
    @Nullable
    private AppCompatTextView titleTextView;

    public LargeHeaderView(@NonNull Context context) {
        super(context);
        init(null, 0);
    }

    public LargeHeaderView(@NonNull Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public LargeHeaderView(@NonNull Context context, AttributeSet attrs, int theme) {
        super(context, attrs, theme);
        init(attrs, theme);
    }

    public void init(@Nullable AttributeSet attrs, int theme) {
        LayoutInflater.from(getContext()).inflate(R.layout.view_large_header, this, true);
        titleTextView = findViewById(R.id.titleTextView);
    }

    public void setTitle(@Nullable CharSequence title) {
        if (titleTextView != null) {
            titleTextView.setText(title);
        }
    }
}
