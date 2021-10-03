package cz.johnyapps.cheers.adapters.recycleradapters;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import cz.johnyapps.cheers.R;

public abstract class SelectableAdapter<VIEW_HOLDER extends SelectableAdapter<VIEW_HOLDER, ITEM, DATA>.SelectableViewHolder, ITEM, DATA> extends BaseAdapter<VIEW_HOLDER, DATA> {
    @NonNull
    private final Map<Integer, ITEM> selectedItems = new HashMap<>();
    @Nullable
    private OnSelectListener<ITEM> onSelectListener;
    private boolean allowSelection = true;
    private boolean multiSelection = true;
    private boolean canceledSelectionThisClick = false;

    public SelectableAdapter(@NonNull Context context) {
        super(context);
    }

    @Override
    public void onBindViewHolder(@NonNull VIEW_HOLDER holder, int position) {
        boolean selected = isSelected(position);
        holder.itemView.setOnLongClickListener(v -> {
            selectPosition(position);
            return true;
        });

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
        selectedItems.clear();

        if (onSelectListener != null) {
            onSelectListener.onSelect(null);
        }

        super.update(data);
    }

    @NonNull
    public abstract ITEM getItem(int position);

    public boolean isSelected(int position) {
        return selectedItems.get(position) != null;
    }

    public boolean isSelecting() {
        return !selectedItems.isEmpty();
    }

    public void selectPosition(int pos) {
        if (!allowSelection) {
            return;
        }

        int oldItemPos = selectedItems.keySet().isEmpty() ? -1 : selectedItems.keySet().toArray(new Integer[0])[0];
        boolean wasAlreadySelected = isSelected(pos);

        if (wasAlreadySelected || pos < 0) {
            selectedItems.remove(pos);
        } else {
            if (!multiSelection && oldItemPos >= 0) {
                selectedItems.remove(oldItemPos);
                notifyItemChanged(oldItemPos);
            }

            ITEM selectedItem = getItem(pos);
            selectedItems.put(pos, selectedItem);
        }

        notifyItemChanged(pos);

        if (onSelectListener != null) {
            onSelectListener.onSelect(selectedItems.values());
        }
    }

    public void cancelSelection() {
        Collection<Integer> positions = new ArrayList<>(selectedItems.keySet());
        for (int pos : positions) {
            selectPosition(pos);
        }
    }

    public void selectAllOrCancelSelection() {
        boolean allSelected = true;

        for (int i = 0; i < getItemCount(); i++) {
            if (!isSelected(i)) {
                allSelected = false;
                selectPosition(i);
            }
        }

        if (allSelected) {
            cancelSelection();
        }
    }

    public boolean canceledSelectionThisClick() {
        return canceledSelectionThisClick;
    }

    public void setAllowSelection(boolean allowSelection) {
        this.allowSelection = allowSelection;
    }

    public boolean isAllowSelection() {
        return allowSelection;
    }

    public void setMultiSelection(boolean multiSelection) {
        this.multiSelection = multiSelection;
    }

    public class SelectableViewHolder extends BaseViewHolder {
        public SelectableViewHolder(@NonNull View root) {
            super(root);
            root.setOnLongClickListener(v -> {
                selectPosition(getAdapterPosition());
                return false;
            });
            addOnRootClickListener(v -> {
                if (isSelecting()) {
                    selectPosition(getAdapterPosition());
                    canceledSelectionThisClick = selectedItems.isEmpty();
                } else {
                    canceledSelectionThisClick = false;
                }
            });
        }
    }

    public void setOnSelectListener(@Nullable OnSelectListener<ITEM> onSelectListener) {
        this.onSelectListener = onSelectListener;
    }

    public interface OnSelectListener<ITEM> {
        void onSelect(@Nullable Collection<ITEM> selectedItems);
    }
}
