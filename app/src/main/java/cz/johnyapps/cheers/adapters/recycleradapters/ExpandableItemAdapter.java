package cz.johnyapps.cheers.adapters.recycleradapters;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public abstract class ExpandableItemAdapter<VIEW_HOLDER extends ExpandableItemAdapter<VIEW_HOLDER, ITEM>.ExpandableItemViewHolder, ITEM> extends FilterableAdapter<VIEW_HOLDER, ITEM> {
    private int expandedItemPosition = -1;

    public ExpandableItemAdapter(@NonNull Context context, @Nullable List<ITEM> items) {
        super(context, items);
    }

    @Override
    public void onBindViewHolder(@NonNull VIEW_HOLDER holder, int position, boolean selected) {
        View expandView = holder.getExpandView(holder.itemView);
        boolean expanded = expandedItemPosition == position;

        if (expanded) {
            expandView.setVisibility(View.VISIBLE);
        } else {
            expandView.setVisibility(View.GONE);
        }

        onBindViewHolder(holder, position, selected, expanded);
    }

    public void expandItem(int position) {
        int oldPosition = expandedItemPosition;
        expandedItemPosition = oldPosition == position ? -1 : position;
        notifyItemChanged(oldPosition);
        notifyItemChanged(position);
    }

    public abstract void onBindViewHolder(@NonNull VIEW_HOLDER holder, int position, boolean selected, boolean expanded);

    public abstract class ExpandableItemViewHolder extends SelectableAdapter<VIEW_HOLDER, ITEM, List<ITEM>>.SelectableViewHolder {
        public ExpandableItemViewHolder(@NonNull View root) {
            super(root);
            addOnRootClickListener(v -> {
                if (!isSelecting() && !canceledSelectionThisClick()) {
                    expandItem(getAdapterPosition());
                }
            });
        }

        @NonNull
        public abstract View getExpandView(@NonNull View root);
    }
}
