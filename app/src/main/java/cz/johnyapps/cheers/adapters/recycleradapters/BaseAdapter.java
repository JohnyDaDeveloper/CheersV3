package cz.johnyapps.cheers.adapters.recycleradapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseAdapter<VIEW_HOLDER extends BaseAdapter.BaseViewHolder, DATA> extends RecyclerView.Adapter<VIEW_HOLDER> {
    @NonNull
    private final Context context;
    @NonNull
    private final LayoutInflater inflater;
    @Nullable
    private OnDataSetChangedListener onDataSetChangedListener;

    public BaseAdapter(@NonNull Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);

        registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                if (onDataSetChangedListener != null) {
                    onDataSetChangedListener.onChange(isEmpty());
                }
            }
        });
    }

    public void update(@Nullable DATA data) {
        onUpdate(data);
        notifyDataSetChanged();
    }

    protected abstract void onUpdate(@Nullable DATA data);

    public abstract boolean isEmpty();

    @NonNull
    public Context getContext() {
        return context;
    }

    @NonNull
    public LayoutInflater getInflater() {
        return inflater;
    }

    public interface OnDataSetChangedListener {
        void onChange(boolean empty);
    }

    public void setOnDataSetChangedListener(@Nullable OnDataSetChangedListener onDataSetChangedListener) {
        this.onDataSetChangedListener = onDataSetChangedListener;
    }

    public static class BaseViewHolder extends RecyclerView.ViewHolder {
        @NonNull
        private final List<View.OnClickListener> onRootClickListeners = new ArrayList<>();

        public BaseViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(v -> {
                for (View.OnClickListener listener : onRootClickListeners) {
                    listener.onClick(v);
                }
            });
        }

        public void addOnRootClickListener(@NonNull View.OnClickListener onClickListener) {
            onRootClickListeners.add(onClickListener);
        }

        public void removeOnRootClickListener(@NonNull View.OnClickListener onClickListener) {
            onRootClickListeners.remove(onClickListener);
        }
    }
}
