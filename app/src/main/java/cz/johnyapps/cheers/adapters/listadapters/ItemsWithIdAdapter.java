package cz.johnyapps.cheers.adapters.listadapters;

import android.content.Context;
import android.view.View;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import java.util.List;

import cz.johnyapps.cheers.entities.ItemWithId;
import cz.johnyapps.cheers.R;
import cz.johnyapps.cheers.tools.TextUtils;

public class ItemsWithIdAdapter<ITEM extends ItemWithId> extends BaseListAdapter<ITEM> implements Filterable {
    public ItemsWithIdAdapter(@NonNull Context context, @NonNull OnItemClickListener<ITEM> onItemClickListener) {
        super(context, onItemClickListener);
    }

    @Override
    protected void onUpdate(@Nullable List<ITEM> items) {

    }

    @Override
    protected boolean filterItem(@NonNull CharSequence constraint, @NonNull ITEM item) {
        return TextUtils.removeDiacritics(item.getText(getContext()).toLowerCase())
                .toString()
                .contains(constraint.toString().toLowerCase());
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getId();
    }

    @Override
    protected int getViewLayoutId() {
        return R.layout.item_item_with_id;
    }

    @Override
    protected void fillView(@NonNull View view, int position) {
        ItemWithId itemWithId = getItem(position);

        AppCompatTextView nameTextView = view.findViewById(R.id.nameTextView);
        nameTextView.setText(itemWithId.getText(getContext()));

        View divider = view.findViewById(R.id.divider);
        if (position == getCount() - 1) {
            divider.setVisibility(View.GONE);
        } else {
            divider.setVisibility(View.VISIBLE);
        }
    }
}
