package cz.johnyapps.cheers.views;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

public class CustomCoordinatorLayout extends CoordinatorLayout {
    @Nullable
    private OnSizeChangedListener onSizeChangedListener;

    public CustomCoordinatorLayout(Context context) {
        super(context);
        init(null, 0);
    }

    public CustomCoordinatorLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public CustomCoordinatorLayout(Context context, AttributeSet attrs, int theme) {
        super(context, attrs, theme);
        init(attrs, theme);
    }

    public void init(@Nullable AttributeSet attrs, int theme) {

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        if (onSizeChangedListener != null) {
            onSizeChangedListener.onSizeChanged(w, h);
        }
    }

    public void setOnSizeChangedListener(@Nullable OnSizeChangedListener onSizeChangedListener) {
        this.onSizeChangedListener = onSizeChangedListener;
    }

    public interface OnSizeChangedListener {
        void onSizeChanged(int width, int height);
    }
}
