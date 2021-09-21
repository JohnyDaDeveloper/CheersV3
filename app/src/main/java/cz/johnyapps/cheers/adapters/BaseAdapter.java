package cz.johnyapps.cheers.adapters;

import android.content.Context;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

public abstract class BaseAdapter<VIEW_HOLDER extends RecyclerView.ViewHolder, DATA> extends RecyclerView.Adapter<VIEW_HOLDER> {
    @NonNull
    private final Context context;
    @NonNull
    private final LayoutInflater inflater;

    public BaseAdapter(@NonNull Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    public void update(@Nullable DATA data) {
        onUpdate(data);
        notifyDataSetChanged();
    }

    protected abstract void onUpdate(@Nullable DATA data);

    @NonNull
    public Context getContext() {
        return context;
    }

    @NonNull
    public LayoutInflater getInflater() {
        return inflater;
    }
}
