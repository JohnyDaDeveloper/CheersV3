package cz.johnyapps.cheers.adapters.recycleradapters;

import android.content.Context;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import cz.johnyapps.cheers.tools.TextUtils;

public abstract class FilterableAdapter<VIEW_HOLDER extends SelectableAdapter<VIEW_HOLDER, ITEM, List<ITEM>>.SelectableViewHolder, ITEM> extends SelectableAdapter<VIEW_HOLDER, ITEM, List<ITEM>>  implements Filterable {
    @NonNull
    private List<ITEM> items;
    @Nullable
    private List<ITEM> filteredItems = null;

    public FilterableAdapter(@NonNull Context context,
                             @Nullable List<ITEM> items) {
        super(context);
        this.items = items == null ? new ArrayList<>() : items;
    }

    @Override
    protected void onUpdate(@Nullable List<ITEM> items) {
        this.items = items == null ? new ArrayList<>() : items;
        this.filteredItems = null;
    }

    @NonNull
    @Override
    public ITEM getItem(int position) {
        return getShownList().get(position);
    }

    @Override
    public int getItemCount() {
        return getShownList().size();
    }

    @NonNull
    public List<ITEM> getShownList() {
        if (filteredItems == null) {
            return items;
        }

        return filteredItems;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                constraint = TextUtils.removeDiacritics(constraint);
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

    public boolean isFiltered() {
        return filteredItems != null && filteredItems.size() != items.size();
    }

    public void clearFilter() {
        filteredItems = null;
        notifyDataSetChanged();
    }

    protected abstract boolean filterItem(@NonNull CharSequence constraint, @NonNull ITEM item);

    public void filter(@NonNull String query) {
        getFilter().filter(query);
    }
}
