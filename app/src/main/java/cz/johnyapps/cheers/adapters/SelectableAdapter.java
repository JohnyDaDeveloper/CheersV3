package cz.johnyapps.cheers.adapters;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import cz.johnyapps.cheers.R;

public abstract class SelectableAdapter<VIEW_HOLDER extends SelectableAdapter<VIEW_HOLDER, ITEM, DATA>.SelectableViewHolder, ITEM, DATA> extends BaseAdapter<VIEW_HOLDER, DATA> {
    @Nullable
    private SelectedItem<ITEM> selectedItem;
    @Nullable
    private OnSelectListener<ITEM> onSelectListener;
    private boolean allowSelection = true;

    public SelectableAdapter(@NonNull Context context) {
        super(context);
    }

    @Override
    public void onBindViewHolder(@NonNull VIEW_HOLDER holder, int position) {
        boolean selected = selectedItem != null && selectedItem.getPosition() == position;

        if (selected) {
            holder.itemView.setForeground(ResourcesCompat.getDrawable(getContext().getResources(),
                    R.drawable.selected_item_background,
                    getContext().getTheme()));
        } else {
            holder.itemView.setForeground(null);
        }

        onBindViewHolder(holder, position, selected);
    }

    public abstract void onBindViewHolder(@NonNull VIEW_HOLDER holder, int position, boolean selected);

    @Override
    public void update(@Nullable DATA data) {
        selectedItem = null;

        if (onSelectListener != null) {
            onSelectListener.onSelect(null);
        }

        super.update(data);
    }

    @NonNull
    public abstract ITEM getItem(int position);

    public static class SelectedItem<ITEM> {
        @NonNull
        private final ITEM selectedItem;
        private final int position;

        public SelectedItem(@NonNull ITEM selectedItem, int position) {
            this.selectedItem = selectedItem;
            this.position = position;
        }

        @NonNull
        public ITEM getSelectedItem() {
            return selectedItem;
        }

        public int getPosition() {
            return position;
        }
    }

    public void selectPosition(int pos) {
        if (!allowSelection) {
            return;
        }

        SelectedItem<ITEM> oldItem = selectedItem;

        if (oldItem != null && oldItem.getPosition() == pos || pos < 0) {
            selectedItem = null;
        } else {
            selectedItem = new SelectedItem<>(getItem(pos), pos);
        }

        if (oldItem != null) {
            notifyItemChanged(oldItem.getPosition());
        }

        if (selectedItem != null && (oldItem == null || oldItem.getPosition() != selectedItem.getPosition())) {
            notifyItemChanged(selectedItem.getPosition());
        }

        if (onSelectListener != null) {
            onSelectListener.onSelect(selectedItem == null ? null : selectedItem.getSelectedItem());
        }
    }

    public void setAllowSelection(boolean allowSelection) {
        this.allowSelection = allowSelection;
    }

    public boolean isAllowSelection() {
        return allowSelection;
    }

    public class SelectableViewHolder extends RecyclerView.ViewHolder {
        public SelectableViewHolder(@NonNull View root) {
            super(root);
            root.setOnLongClickListener(v -> {
                selectPosition(getAdapterPosition());
                return false;
            });
            root.setOnClickListener(v -> {
                if (selectedItem != null) {
                    selectPosition(getAdapterPosition());
                }
            });
        }
    }

    public void setOnSelectListener(@Nullable OnSelectListener<ITEM> onSelectListener) {
        this.onSelectListener = onSelectListener;
    }

    public interface OnSelectListener<ITEM> {
        void onSelect(@Nullable ITEM selectedItem);
    }
}
