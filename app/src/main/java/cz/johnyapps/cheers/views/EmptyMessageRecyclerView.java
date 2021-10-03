package cz.johnyapps.cheers.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import cz.johnyapps.cheers.R;
import cz.johnyapps.cheers.adapters.recycleradapters.BaseAdapter;

public class EmptyMessageRecyclerView extends LinearLayout {
    @Nullable
    private String emptyMessage = null;
    private RecyclerView recyclerView;
    private View emptyMessageView;

    public EmptyMessageRecyclerView(@NonNull Context context) {
        super(context);
        init(null, 0);
    }

    public EmptyMessageRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public EmptyMessageRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int theme) {
        super(context, attrs, theme);
        init(attrs, theme);
    }

    public void init(@Nullable AttributeSet attrs, int theme) {
        LayoutInflater.from(getContext()).inflate(R.layout.view_empty_message_recycler_view,
                this,
                true);

        if (attrs != null) {
            final TypedArray array = getContext().obtainStyledAttributes(attrs,
                    R.styleable.EmptyMessageRecyclerView,
                    theme,
                    0);

            emptyMessage = array.getString(R.styleable.EmptyMessageRecyclerView_emptyMessage);
            array.recycle();
        }

        AppCompatTextView emptyMessageTextView = findViewById(R.id.emptyMessageTextView);
        assert emptyMessageTextView != null;
        emptyMessageTextView.setText(emptyMessage);

        recyclerView = findViewById(R.id.recyclerView);
        emptyMessageView = findViewById(R.id.emptyMessageLayout);
    }

    private void emptyMessageVisible(boolean visible) {
        if (visible) {
            recyclerView.setVisibility(GONE);
            emptyMessageView.setVisibility(VISIBLE);
        } else {
            recyclerView.setVisibility(VISIBLE);
            emptyMessageView.setVisibility(GONE);
        }
    }

    public void setAdapter(RecyclerView.Adapter<? extends RecyclerView.ViewHolder> adapter) {
        if (adapter instanceof BaseAdapter) {
            BaseAdapter baseAdapter = (BaseAdapter) adapter;
            baseAdapter.setOnDataSetChangedListener(this::emptyMessageVisible);
        }

        recyclerView.setAdapter(adapter);
    }

    public void setLayoutManager(RecyclerView.LayoutManager layoutManager) {
        recyclerView.setLayoutManager(layoutManager);
    }

    public void scrollToPosition(int position) {
        recyclerView.scrollToPosition(position);
    }

    @Override
    public void setOnScrollChangeListener(OnScrollChangeListener l) {
        recyclerView.setOnScrollChangeListener(l);
    }
}
