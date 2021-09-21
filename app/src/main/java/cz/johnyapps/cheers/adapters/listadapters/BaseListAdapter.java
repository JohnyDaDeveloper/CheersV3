package cz.johnyapps.cheers.adapters.listadapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseListAdapter<ITEM> extends BaseAdapter implements Filterable {
    @NonNull
    private final Context context;
    @NonNull
    private final LayoutInflater inflater;
    @NonNull
    private List<ITEM> items = new ArrayList<>();
    @Nullable
    private List<ITEM> filteredItems;
    @Nullable
    private final OnItemClickListener<ITEM> onItemClickListener;

    public BaseListAdapter(@NonNull Context context, @Nullable OnItemClickListener<ITEM> onItemClickListener) {
        super();
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.onItemClickListener = onItemClickListener;
    }

    public void update(@Nullable List<ITEM> items) {
        onUpdate(items);
        this.items = items == null ? new ArrayList<>() : items;
        this.filteredItems = null;
        notifyDataSetChanged();
    }

    protected void onUpdate(@Nullable List<ITEM> items) {

    }

    protected abstract boolean filterItem(@NonNull CharSequence constraint, @NonNull ITEM item);

    @NonNull
    public Context getContext() {
        return context;
    }

    @NonNull
    public LayoutInflater getInflater() {
        return inflater;
    }

    @Override
    public int getCount() {
        if (filteredItems != null) {
            return filteredItems.size();
        }

        return items.size();
    }

    @Override
    public ITEM getItem(int position) {
        if (filteredItems != null) {
            return filteredItems.get(position);
        }

        return items.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = getInflater().inflate(getViewLayoutId(), parent, false);
        }

        fillView(convertView, position);
        if (onItemClickListener != null) {
            convertView.setOnClickListener(v -> onItemClickListener.onItemClick(getItem(position)));
        }

        return convertView;
    }

    @LayoutRes
    protected abstract int getViewLayoutId();

    protected abstract void fillView(@NonNull View view, int position);

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                List<ITEM> filtered = new ArrayList<>();

                for (ITEM item : items) {
                    if (filterItem(constraint, item)) {
                        filtered.add(item);
                    }
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filtered;
                filterResults.count = filtered.size();
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredItems = (List<ITEM>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface OnItemClickListener<ITEM> {
        void onItemClick(@NonNull ITEM item);
    }
}
